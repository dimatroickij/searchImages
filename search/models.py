import uuid

from django.db import models
from django.template.defaultfilters import slugify
from sorl.thumbnail import ImageField

# Create your models here.
class Image(models.Model):
    unique_id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    title = models.CharField('Название изображения', max_length=255)
    slug = models.SlugField(max_length=200, blank=True)
    image = ImageField('Изображение', null=True, upload_to='')
    description = models.CharField('Описание', max_length=255, blank=True, null=True)
    created = models.DateField(auto_now_add=True, db_index=True, null=True)

    def __str__(self):
        return self.title

    def save(self, *args, **kwargs):
        if not self.slug:
            self.slug = slugify(self.title)
            super(Image, self).save(*args, **kwargs)

class Pattern(models.Model):
    unique_id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    image = models.ForeignKey(Image, on_delete=models.CASCADE, verbose_name='Изображение')
    name = models.CharField('Название образа', max_length=255)
    score = models.FloatField('Вероятность нахождения образа на изображении')