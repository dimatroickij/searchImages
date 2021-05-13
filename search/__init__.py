from search.yolo3.utils import Load_Yolo_model, read_class_names, YOLO_COCO_CLASSES

yolo = Load_Yolo_model()
NUM_CLASS = read_class_names(YOLO_COCO_CLASSES)
print(NUM_CLASS)