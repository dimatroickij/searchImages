from django import forms
from django.template.defaultfilters import slugify

from search.models import Image


class ImageForm(forms.ModelForm):
    class Meta:
        model = Image
        fields = ('title', 'image')
        widgets = {'title': forms.TextInput(attrs={'class': 'form-control'}),
                   'image': forms.FileInput(attrs={'class': 'form-control-file'})}