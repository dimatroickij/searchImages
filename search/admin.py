from django.contrib import admin

# Register your models here.
from search.models import Image, Pattern

admin.site.register(Image)

admin.site.register(Pattern)