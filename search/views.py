import json

import PIL
import requests
import numpy as np
import tensorflow as tf
from django.contrib import messages
from django.contrib.auth.decorators import login_required
from django.core.paginator import Paginator
from django.http import HttpResponse, JsonResponse
from django.shortcuts import render, redirect
from object_detection.utils import visualization_utils as viz_utils

from sorl.thumbnail import get_thumbnail

from search import utils
from search.forms import ImageForm
from search.models import Image
from searchImages.settings import BASE_DIR, detect_fn, category_index

def home(request):
    return render(request, 'search/home.html')


def remap_keys(mapping):
    return [{str(k): v} for k, v in mapping.items()]


color_elements = utils.load_hist_elements_from_json(BASE_DIR + '/static/elements.json')
grid_1d = utils.generate_positional_grid_1d(5, 5)


@login_required
def all(request):
    form = ImageForm()
    images = Image.objects.all().order_by('title')
    paginator = Paginator(images, 8)
    pageNumber = request.GET.get('page')
    currentPage = paginator.get_page(pageNumber)
    return render(request, 'search/all.html', {'images': currentPage, 'form': form})


@login_required
def search(request):
    def getGraph(pk):
        hist = json.loads(Image.objects.get(pk=pk).histogram)
        labels = list(map(lambda x: list(x.keys())[0], hist))
        data = list(map(lambda x: list(x.values())[0], hist))
        return json.dumps({'type': 'bar',
                           'data': {
                               'labels': labels,
                               'datasets': [{
                                   'label': 'Значение гистограммы',
                                   'data': data,
                                   'backgroundColor': ['rgba(153, 102, 255, 0.2)'] * len(data),
                                   'borderColor': ['rgba(153, 102, 255, 1)'] * len(data),
                                   'borderWidth': 1
                               }]},
                           'options': {'scales': {'yAxes': [{'ticks': {'beginAtZero': True}}]}}})

    try:
        query = request.GET['query']
    except:
        return HttpResponse(status=400)
    if query == '':
        messages.add_message(request, messages.WARNING, "Введите поисковый запрос!")
        paginator = Paginator([], 3)
        pageNumber = request.GET.get('page')
        currentPage = paginator.get_page(pageNumber)
        return render(request, 'search/search.html', {'query': query, 'images': currentPage})
    searchRequest = requests.get('http://histogram:8080/search', data=query)
    if searchRequest.status_code == 200:
        sortedTuples = sorted(searchRequest.json().items(), key=lambda item: item[1], reverse=True)
        response = {k: v for k, v in sortedTuples}
        #
        # paginator = Paginator(images, 8)
        # pageNumber = request.GET.get('page')
        # currentPage = paginator.get_page(pageNumber)
        # return render(request, 'search/all.html', {'images': currentPage, 'form': form})

        images = list(map(lambda x: {'pk': x[0], 'result': round(x[1], 2), 'image': Image.objects.get(pk=x[0]).image,
                                     'title': Image.objects.get(pk=x[0]).title, 'graph': getGraph(x[0])},
                          response.items()))
        paginator = Paginator(images, 3)
        pageNumber = request.GET.get('page')
        currentPage = paginator.get_page(pageNumber)
        return render(request, 'search/search.html', {'query': query, 'images': currentPage})
    else:
        return HttpResponse(status=404)


@login_required
def upload(request):
    try:
        if request.method == 'POST':
            form = ImageForm(request.POST, request.FILES)
            if form.is_valid():
                img = form.save()
                image = Image.objects.get(pk=img.pk)
                histogram = json.dumps(remap_keys(utils.convert2hist_1d(PIL.Image.open(image.image), color_elements,
                                                                        grid_1d).to_dict()))
                width, height = PIL.Image.open(image.image).size
                img.width = width
                img.height = height
                img.histogram = histogram
                sendHist = requests.post('http://histogram:8080/addHistogram/' + str(image.pk),
                                         json=json.loads(histogram))
                if sendHist.status_code != 200:
                    messages.add_message(request, messages.WARNING,
                                         'Ошибка при индексации в Lucene. Изображение не добавлено')
                img.save()
                return redirect('search:all')
    except:
        messages.add_message(request, messages.WARNING,
                             'Ошибка при добавлении изображения')
        return redirect('search:all')
    return HttpResponse(status=400)


@login_required
def delete(request, pk):
    if Image.objects.filter(pk=pk).exists():
        img = Image.objects.get(pk=pk)
        from sorl.thumbnail import delete
        deleteHist = requests.get('http://histogram:8080/deleteHistogram/' + str(img.pk))
        if deleteHist.status_code == 200:
            delete(img.image)
            img.delete()
        else:
            messages.add_message(request, messages.WARNING,
                                 'Ошибка удаления в Lucene. Изображение не удалено')
        return redirect('search:all')
    return HttpResponse(status=204)


def feedback(request):
    pass


@login_required
def rescan(request, pk):
    image = Image.objects.get(pk=pk)
    histogram = json.dumps(remap_keys(utils.convert2hist_1d(PIL.Image.open(image.image), color_elements,
                                                            grid_1d).to_dict()))
    rescanHist = requests.post('http://histogram:8080/rescanHistogram/' + str(image.pk), json=json.loads(histogram))
    if rescanHist.status_code == 200:
        image.histogram = histogram
        image.save()
    else:
        messages.add_message(request, messages.WARNING,
                             'Ошибка обновления данных в Lucene. Попробуйте ещё раз.')
    return redirect('search:all')


def segmentObjectDetections(path):
    image = Image.open(path)
    image_np = np.array(image)

    width, height = image.size

    input_tensor = tf.convert_to_tensor(image_np)
    input_tensor = input_tensor[tf.newaxis, ...]

    detections = detect_fn(input_tensor)

    num_detections = int(detections.pop('num_detections'))
    detections = {key: value[0, :num_detections].numpy()
                  for key, value in detections.items()}
    detections['num_detections'] = num_detections

    detections['detection_classes'] = detections['detection_classes'].astype(np.int64)

    image_np_with_detections = image_np.copy()

    viz_utils.visualize_boxes_and_labels_on_image_array(
        image_np_with_detections,
        detections['detection_boxes'],
        detections['detection_classes'],
        detections['detection_scores'],
        category_index,
        use_normalized_coordinates=True,
        max_boxes_to_draw=200,
        min_score_thresh=.30,
        agnostic_mode=False)

    dataImage = {'segments': [], 'width': width, 'height': height}
    for i in range(0, detections['num_detections']):
        dataImage['segments'].append({'scores': detections['detection_scores'][i],
                                      'boxes': list(detections['detection_boxes'])[i],
                                      'classes': list(map(lambda x: str(x), detections['detection_classes']))[i],
                                      })
    return dataImage


@login_required
def detail(request, pk):
    if Image.objects.filter(pk=pk).exists():
        img = Image.objects.get(pk=pk)
        im = get_thumbnail(img.image, '300x300', crop='center', quality=99)
        hist = json.loads(img.histogram)
        labels = list(map(lambda x: list(x.keys())[0], hist))
        data = list(map(lambda x: list(x.values())[0], hist))
        response = {'type': 'bar',
                    'data': {
                        'labels': labels,
                        'datasets': [{
                            'label': 'Значение гистограммы',
                            'data': data,
                            'backgroundColor': ['rgba(153, 102, 255, 0.2)'] * len(data),
                            'borderColor': ['rgba(153, 102, 255, 1)'] * len(data),
                            'borderWidth': 1
                        }]},
                    'options': {'scales': {'yAxes': [{'ticks': {'beginAtZero': True}}]}}}
        return JsonResponse({'img': {'url': im.url, 'width': im.width, 'height': im.height}, 'graph': response},
                            safe=False)
    return HttpResponse(status=204)
