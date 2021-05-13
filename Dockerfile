FROM ubuntu:20.04
ENV TZ=Europe/Moscow
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
RUN apt-get update
RUN apt-get install -y python3-pip python3-dev python3-virtualenv git curl wget unzip python3-opencv

RUN wget https://pjreddie.com/media/files/yolov3.weights
WORKDIR /usr/src/app

ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

COPY . .
RUN pip3 install pip-tools
RUN pip-compile --upgrade
RUN pip3 install -r requirements.txt