import json

import PIL
import requests
from django.contrib import messages
from django.contrib.auth.decorators import login_required
from django.core.paginator import Paginator
from django.http import HttpResponse, JsonResponse
from django.shortcuts import render, redirect
from sorl.thumbnail import get_thumbnail

from search import utils, category_index
from search.forms import ImageForm
from search.models import Image
from search.utils import segmentObjectDetections, generate_positional_grid_1d, get_positional_grid_1d, \
    create_position_mask, create_object_mask, create_histogram


def remap_keys(mapping):
    return [{str(k): v} for k, v in mapping.items()]


# Преобразование кода объекта в его название
def editLabels(label):
    pos = label.split(',')[0].replace("'", '').replace('(', '')
    obj = category_index[int(label.split(',')[1].replace(')', '').replace("'", ''))]['name']
    return f"{pos}, {obj}"

# Подготовка JSON файлов для отправки в ChartJS
def formationHistogram(histogram):
    hist = json.loads(histogram)
    labels = list(map(lambda x: editLabels(list(x.keys())[0]), hist))
    data = list(map(lambda x: list(x.values())[0], hist))
    return {'type': 'bar',
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


grid_1d = utils.generate_positional_grid_1d(5, 5)


def home(request):
    return render(request, 'search/home.html')


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

        images = list(map(lambda x: {'pk': x[0], 'result': round(x[1], 2), 'image': Image.objects.get(pk=x[0]).image,

                                     'title': Image.objects.get(pk=x[0]).title,
                                     'graph': json.dumps(formationHistogram(Image.objects.get(pk=x[0]).histogram))},
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
                dataImage = segmentObjectDetections(PIL.Image.open(image.image))
                grid = generate_positional_grid_1d(5, 5)
                position_elements = get_positional_grid_1d(dataImage['width'], dataImage['height'], grid)
                pos_mask = create_position_mask(dataImage['width'], dataImage['height'], position_elements)
                obj_mask = create_object_mask(dataImage['width'], dataImage['height'], dataImage['segments'])
                histogram = json.dumps(
                    remap_keys(create_histogram(dataImage['width'], dataImage['height'], pos_mask, obj_mask).to_dict()))

                img.width = dataImage['width']
                img.height = dataImage['height']
                img.details = dataImage['segments']
                img.histogram = histogram
                sendHist = requests.post('http://histogram:8080/addHistogram/' + str(image.pk),
                                         json=json.loads(histogram))
                if sendHist.status_code != 200:
                    messages.add_message(request, messages.WARNING,
                                         'Ошибка при индексации в Lucene. Изображение не добавлено')
                img.save()
                return redirect('search:all')
    except:
        messages.add_message(request, messages.WARNING, 'Ошибка при добавлении изображения')
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
            messages.add_message(request, messages.WARNING, 'Ошибка удаления в Lucene. Изображение не удалено')
        return redirect('search:all')
    return HttpResponse(status=204)


@login_required
def detail(request, pk):
    if Image.objects.filter(pk=pk).exists():
        img = Image.objects.get(pk=pk)
        im = get_thumbnail(img.image, '300x300', crop='center', quality=99)
        return JsonResponse({'img': {'url': im.url, 'width': im.width, 'height': im.height},
                             'graph': formationHistogram(img.histogram)}, safe=False)
    return HttpResponse(status=204)
