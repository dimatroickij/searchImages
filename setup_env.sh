#!/bin/sh

echo DEBUG=$DEBUG >> .env
echo DJANGO_ALLOWED_HOSTS=$DJANGO_ALLOWED_HOSTS >> .env
echo SECRET_KEY=$SECRET_KEY >> .env
echo POSTGRES_DB=$POSTGRES_DB >> .env
echo SQL_ENGINE=$SQL_ENGINE >> .env
echo SQL_HOST=$SQL_HOST >> .env
echo POSTGRES_PASSWORD=$POSTGRES_PASSWORD >> .env
echo SQL_PORT=$SQL_PORT >> .env
echo POSTGRES_USER=$POSTGRES_USER >> .env
echo CI_REGISTRY_USER=$CI_REGISTRY_USER   >> .env
echo CI_JOB_TOKEN=$CI_JOB_TOKEN  >> .env
echo CI_REGISTRY=$CI_REGISTRY  >> .env
echo IMAGE=$CI_REGISTRY/$CI_PROJECT_NAMESPACE/$CI_PROJECT_NAME >> .env
echo EMAIL_HOST_USER=$EMAIL_HOST_USER >> .env
echo EMAIL_HOST_PASSWORD=$EMAIL_HOST_PASSWORD >> .env
