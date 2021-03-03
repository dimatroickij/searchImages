import tensorflow as tf
from object_detection.utils import label_map_util
from searchImages.settings import SAVED_MODEL_DIR, LABELS_DIR

model = tf.saved_model.load(SAVED_MODEL_DIR)
detect_fn = model.signatures['serving_default']
category_index = label_map_util.create_category_index_from_labelmap(LABELS_DIR, use_display_name=True)