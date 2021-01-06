# Generated by Django 3.1.4 on 2020-12-22 09:41

from django.db import migrations, models
import django.db.models.deletion
import uuid


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Image',
            fields=[
                ('unique_id', models.UUIDField(default=uuid.uuid4, editable=False, primary_key=True, serialize=False)),
                ('name', models.CharField(max_length=255, verbose_name='Название изображения')),
                ('src', models.CharField(max_length=255, verbose_name='Ссылка на изображение')),
                ('description', models.CharField(blank=True, max_length=255, null=True, verbose_name='Описание')),
            ],
        ),
        migrations.CreateModel(
            name='Pattern',
            fields=[
                ('unique_id', models.UUIDField(default=uuid.uuid4, editable=False, primary_key=True, serialize=False)),
                ('name', models.CharField(max_length=255, verbose_name='Название образа')),
                ('score', models.FloatField(verbose_name='Вероятность нахождения образа на изображении')),
                ('image', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='search.image', verbose_name='Изображение')),
            ],
        ),
    ]