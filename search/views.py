from django.http import HttpResponse
from django.shortcuts import render


# Create your views here.
from search.forms import ImageForm


def home(request):
    return render(request, 'home.html')


def search(request):
    pass

def upload(request):
    if request.method == 'GET':
        form = ImageForm()
        return render(request, 'upload.html', {'form': form})
    elif request.method == 'POST':
        form = ImageForm(request.POST, request.FILES)
        if form.is_valid():
            form.save()
            img_obj = form.instance
            return render(request, 'upload.html', {'form': form, 'img_obj': img_obj})
    return HttpResponse(status=404)

def delete(request):
    if request.method == 'GET':
        return render(request, 'delete.html')
    elif request.method == 'POST':
        return HttpResponse(status=200)
    return HttpResponse(status=404)

def feedback(request):
    pass

def patch(request):
    pass

def detail(request, pk):
    pass