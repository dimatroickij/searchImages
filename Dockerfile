FROM ubuntu

RUN apt-get update
RUN apt-get install -y python3-pip python3-dev python3-virtualenv git curl wget unzip
RUN pip3 install tensorflow jupyter
RUN git clone https://github.com/tensorflow/models.git
RUN wget https://github.com/protocolbuffers/protobuf/releases/download/v3.15.3/protoc-3.15.3-linux-x86_64.zip
RUN unzip protoc-3.15.3-linux-x86_64.zip
RUN echo "export PATH=$PATH:/protoc-3.15.3-linux-x86_64/bin" > /etc/environment
RUN cd /models/research/ && protoc object_detection/protos/*.proto --python_out=.
RUN git clone https://github.com/cocodataset/cocoapi.git
RUN cp -r cocoapi/PythonAPI/pycocotools /models/research/
RUN cd /models/research/ && cp object_detection/packages/tf2/setup.py . && python3 -m pip install .
RUN cd /models/research/ && python3 object_detection/builders/model_builder_tf2_test.py

RUN wget http://download.tensorflow.org/models/object_detection/ssd_mobilenet_v2_coco_2018_03_29.tar.gz
RUN tar -zxvf /ssd_mobilenet_v2_coco_2018_03_29.tar.gz

RUN wget https://raw.githubusercontent.com/tensorflow/models/master/research/object_detection/data/mscoco_label_map.pbtxt
WORKDIR /usr/src/app

ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

COPY . .
RUN pip3 install pip-tools
RUN pip-compile --upgrade
RUN pip3 install -r requirements.txt