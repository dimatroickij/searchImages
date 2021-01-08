from datetime import datetime

from django.contrib.auth import get_user_model
from django.core.mail import EmailMultiAlternatives
from django.db import models

# Create your models here.
from django.db.models.signals import post_save
from django.dispatch import receiver
from django.template.loader import render_to_string
from django.utils.encoding import force_bytes
from django.utils.http import urlsafe_base64_encode

from authentication.tokens import TokenGenerator
from searchImages.settings import SITE_PROTOCOL, SITE_URL


@receiver(post_save, sender=get_user_model())
def user_post_save(instance, created, *args, **kwargs):
    if created:
        UserModel = get_user_model()
        user = UserModel.objects.get(pk=instance.pk)
        mail_subject = 'Активация аккаунта на портале управления умным домом'
        message = render_to_string('registration/confirmEmail.html', {
            'user': user,
            'uid': urlsafe_base64_encode(force_bytes(user.pk)),
            'token': TokenGenerator().make_token(user),
            'year': datetime.now(),
            'protocol': SITE_PROTOCOL,
            'url': SITE_URL
        })
        email = EmailMultiAlternatives(mail_subject, message, to=[user.email], )
        email.attach_alternative(message, 'text/html')
        email.content_subtype = 'html'
        email.send()