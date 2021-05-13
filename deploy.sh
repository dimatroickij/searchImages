#!/bin/sh

ssh -o StrictHostKeyChecking=no dimatroickij@$IP_ADDRESS << 'ENDSSH'
  cd /home/dimatroickij/applications/searchimages
  export $(cat .env | xargs)
  docker login -u $CI_REGISTRY_USER -p $CI_JOB_TOKEN $CI_REGISTRY
  docker pull $IMAGE:web
  docker pull $IMAGE:histogram
  docker pull $IMAGE:nginx
  docker-compose -f docker-compose.prod.yml up -d
  docker system prune
ENDSSH
