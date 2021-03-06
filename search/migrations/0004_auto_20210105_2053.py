# Generated by Django 3.1.5 on 2021-01-05 20:53

from django.db import migrations, models
import sorl.thumbnail.fields


class Migration(migrations.Migration):

    dependencies = [
        ('search', '0003_auto_20210105_2045'),
    ]

    operations = [
        migrations.RenameField(
            model_name='image',
            old_name='name',
            new_name='title',
        ),
        migrations.AddField(
            model_name='image',
            name='created',
            field=models.DateField(auto_now_add=True, db_index=True, null=True),
        ),
        migrations.AddField(
            model_name='image',
            name='slug',
            field=models.SlugField(blank=True, max_length=200),
        ),
        migrations.AddField(
            model_name='image',
            name='url',
            field=models.URLField(null=True),
        ),
        migrations.AlterField(
            model_name='image',
            name='image',
            field=sorl.thumbnail.fields.ImageField(null=True, upload_to='dev/img', verbose_name='Изображение'),
        ),
    ]
