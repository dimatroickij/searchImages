import requests
from django.contrib.auth.decorators import login_required
from django.core.paginator import Paginator
from django.http import HttpResponse, JsonResponse
from django.shortcuts import render, redirect

# Create your views here.
from sorl.thumbnail import get_thumbnail

from search.forms import ImageForm
from search.models import Image


def home(request):
    return render(request, 'search/home.html')


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
            form.save()
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
        #sendHistogram = requests.post('localhost:8080/addHistog', json=data)
        #print(sendHistogram.status_code)
        # TODO: отправка запроса в spring-приложение для удаления данных об этом изображении
        return redirect('search:all')
    return HttpResponse(status=204)


def feedback(request):
    pass


@login_required
def rescan(request, pk):
    return redirect('search:all')


@login_required
def detail(request, pk):
    if Image.objects.filter(pk=pk).exists():
        img = Image.objects.get(pk=pk)
        im = get_thumbnail(img.image, '300x300', crop='center', quality=99)
        response = {'type': 'bar',
                    'data': {
                        'labels': ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
                        'datasets': [{
                            'label': '# of Votes',
                            'data': [12, 19, 3, 5, 2, 3],
                            'backgroundColor': [
                                'rgba(255, 99, 132, 0.2)',
                                'rgba(54, 162, 235, 0.2)',
                                'rgba(255, 206, 86, 0.2)',
                                'rgba(75, 192, 192, 0.2)',
                                'rgba(153, 102, 255, 0.2)',
                                'rgba(255, 159, 64, 0.2)'
                            ],
                            'borderColor': [
                                'rgba(255, 99, 132, 1)',
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 206, 86, 1)',
                                'rgba(75, 192, 192, 1)',
                                'rgba(153, 102, 255, 1)',
                                'rgba(255, 159, 64, 1)'
                            ],
                            'borderWidth': 1
                        }]
                    },
                    'options': {'scales': {'yAxes': [{'ticks': {'beginAtZero': True}}]}}
                    }
        return JsonResponse({'img': {'url': im.url, 'width': im.width, 'height': im.height}, 'graph': response},
                            safe=False)
    return HttpResponse(status=204)
