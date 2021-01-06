from django.contrib import admin

# Register your models here.
from sorl.thumbnail.admin import AdminImageMixin

from search.models import Image, Pattern

admin.site.register(Pattern)

class MyModelAdmin(AdminImageMixin, admin.ModelAdmin):
    list_display = ['title', 'slug', 'image', 'created']
    list_filter = ['created']

admin.site.register(Image, MyModelAdmin)