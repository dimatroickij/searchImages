# Generated by Django 3.1.5 on 2021-01-05 20:57

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('search', '0004_auto_20210105_2053'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='image',
            name='url',
        ),
    ]