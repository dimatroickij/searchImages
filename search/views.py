import json

import PIL
import requests
from django.contrib import messages
from django.contrib.auth.decorators import login_required
from django.core.paginator import Paginator
from django.http import HttpResponse, JsonResponse
from django.shortcuts import render, redirect

# Create your views here.
from sorl.thumbnail import get_thumbnail

from search import utils
from search.forms import ImageForm
from search.models import Image
from searchImages.settings import BASE_DIR


def home(request):
    return render(request, 'search/home.html')


def remap_keys(mapping):
    return [{str(k): v} for k, v in mapping.items()]


color_elements = utils.load_hist_elements_from_json(BASE_DIR + '/static/elements.json')
grid_1d = utils.generate_positional_grid_1d(5, 5)


@login_required
def all(request):
    form = ImageForm()
    images = Image.objects.all()
    paginator = Paginator(images, 8)
    pageNumber = request.GET.get('page')
    currentPage = paginator.get_page(pageNumber)
    return render(request, 'search/all.html', {'images': currentPage, 'form': form})


def search(request):
    pass


@login_required
def upload(request):
    if request.method == 'POST':
        form = ImageForm(request.POST, request.FILES)
        if form.is_valid():
            img = form.save()
            image = Image.objects.get(pk=img.pk)
            histogram = json.dumps(remap_keys(utils.convert2hist_1d(PIL.Image.open(image.image), color_elements,
                                                                    grid_1d).to_dict()))
            img.histogram = histogram
            sendHist = requests.post('localhost:8080/addHistogram', json=histogram)
            if sendHist.status_code != 200:
                messages.add_message(request, messages.ERROR,
                                     'Ошибка при индексации в Lucene. Изображение не добавлено')
            img.save()
            return redirect('search:all')
    return HttpResponse(status=400)


@login_required
def delete(request, pk):
    if Image.objects.filter(pk=pk).exists():
        img = Image.objects.get(pk=pk)
        from sorl.thumbnail import delete
        delete(img.image)
        img.delete()
        data = {"e1": 0.4, "e2": 0.1, "e3": 0.3, "e4": 0.1, "e5": 0.1}
        # sendHistogram = requests.post('localhost:8080/addHistog', json=data)
        # print(sendHistogram.status_code)
        # TODO: отправка запроса в spring-приложение для удаления данных об этом изображении
        return redirect('search:all')
    return HttpResponse(status=204)


def feedback(request):
    pass


@login_required
def rescan(request, pk):
    image = Image.objects.get(pk=pk)
    histogram = json.dumps(remap_keys(utils.convert2hist_1d(PIL.Image.open(image.image), color_elements,
                                                            grid_1d).to_dict()))
    image.histogram = histogram
    image.save()
    # sendHist = requests.get('http://histogram:8080/addHistogram', json=histogram)
    # print(sendHist.status_code)
    print(json.loads(histogram))
    return redirect('search:all')


@login_required
def detail(request, pk):
    if Image.objects.filter(pk=pk).exists():
        img = Image.objects.get(pk=pk)
        im = get_thumbnail(img.image, '300x300', crop='center', quality=99)
        hist = json.loads(img.histogram)
        labels = list(map(lambda x: list(x.keys())[0], hist))
        data = list(map(lambda x: list(x.values())[0], hist))
        print(labels)
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
