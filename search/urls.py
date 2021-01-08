from django.urls import path

from search import views

app_name = 'search'

urlpatterns = [
    path('', views.home, name='home'),
    path('all', views.all, name='all'),
    path('search', views.search, name='search'),
    path('upload', views.upload, name='upload'),
    path('delete/<uuid:pk>', views.delete, name='delete'),
    path('feedback', views.feedback, name='feedback'),
    path('rescan/<uuid:pk>', views.rescan, name='rescan'),
    path('detail/<uuid:pk>', views.detail, name='detail'),
]
