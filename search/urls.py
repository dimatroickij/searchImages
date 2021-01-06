from django.urls import path

from search import views

app_name = 'search'

urlpatterns = [
    path('', views.home, name='home'),
    path('search', views.search, name='search'),
    path('upload', views.upload, name='upload'),
    path('delete', views.delete, name='delete'),
    path('feedback', views.feedback, name='feedback'),
    path('patch/<uuid:pk>', views.patch, name='patch'),
    path('detail/<uuid:pk>', views.detail, name='detail')
]
