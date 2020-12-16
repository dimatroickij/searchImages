import uuid

from django.db import models

# Create your models here.
class Image(models.Model):
    unique_id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    name = models.CharField('Название изображения', max_length=255)
    src = models.CharField('Ссылка на изображение', max_length=255)
    description = models.CharField('Описание', max_length=255, blank=True, null=True)

class Pattern(models.Model):
    unique_id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    image = models.ForeignKey(Image, on_delete=models.CASCADE, verbose_name='Изображение')
    name = models.CharField('Название образа', max_length=255)
    score = models.FloatField('Вероятность нахождения образа на изображении')