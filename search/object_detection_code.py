import os
import tensorflow as tf
import time

from object_detection.utils import label_map_util
from object_detection.utils import visualization_utils as viz_utils

import numpy as np
from PIL import Image
import warnings

HOME_DIR = os.getcwd()
IMAGE_DIR = '/home/dimatroickij/segmentation/image2.jpg'
MODEL_DIR = os.path.join(HOME_DIR, 'ssd_mobilenet_v2_coco_2018_03_29')
LABELS_DIR = os.path.join(HOME_DIR, 'mscoco_label_map.pbtxt')

SAVED_MODEL_DIR = MODEL_DIR + "/saved_model"

print('Loading model...', end='')
start_time = time.time()

model = tf.saved_model.load(SAVED_MODEL_DIR)
detect_fn = model.signatures['serving_default']

end_time = time.time()
elapsed_time = end_time - start_time
print('Done! Took {} seconds'.format(elapsed_time))
category_index = label_map_util.create_category_index_from_labelmap(LABELS_DIR, use_display_name=True)

warnings.filterwarnings('ignore')  # Suppress Matplotlib warnings

imagesArray = []

print('Running inference for {}... '.format(IMAGE_DIR))


def segmentObjectDetections(path):
    image = Image.open(path)
    image_np = np.array(image)

    width, height = image.size

    input_tensor = tf.convert_to_tensor(image_np)
    input_tensor = input_tensor[tf.newaxis, ...]

    detections = detect_fn(input_tensor)

    num_detections = int(detections.pop('num_detections'))
    detections = {key: value[0, :num_detections].numpy()
                  for key, value in detections.items()}
    detections['num_detections'] = num_detections

    detections['detection_classes'] = detections['detection_classes'].astype(np.int64)

    image_np_with_detections = image_np.copy()

    viz_utils.visualize_boxes_and_labels_on_image_array(
        image_np_with_detections,
        detections['detection_boxes'],
        detections['detection_classes'],
        detections['detection_scores'],
        category_index,
        use_normalized_coordinates=True,
        max_boxes_to_draw=200,
        min_score_thresh=.30,
        agnostic_mode=False)

    dataImage = {'segments': [], 'width': width, 'height': height}
    for i in range(0, detections['num_detections']):
        dataImage['segments'].append({'scores': detections['detection_scores'][i],
                                      'boxes': list(detections['detection_boxes'])[i],
                                      'classes': list(map(lambda x: str(x), detections['detection_classes']))[i],
                                      })
    return dataImage


print(segmentObjectDetections(IMAGE_DIR))