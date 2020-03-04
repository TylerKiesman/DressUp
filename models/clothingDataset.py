import os
import numpy as np
import torch
import re
from PIL import Image
from xml.dom import minidom


class ClothingDataset(object):
    def __init__(self, root, transforms):
        self.root = root
        self.transforms = transforms
        # load all image files, sorting them to
        # ensure that they are aligned
        self.images = list(sorted(os.listdir(os.path.join(root, "Clothing_Data"))))

    def __getitem__(self, idx):
        # load images ad masks
        img_path = os.path.join(self.root, "Clothing_Data", self.images[idx])
        img = Image.open(img_path).convert("RGB")
        # note that we haven't converted the mask to RGB,
        # because each color corresponds to a different instance
        # with 0 being background
        xml_path = re.sub("\..*", ".xml", img_path)

        xml_file = minidom.parse(xml_path)

        objects = xml_file.getElementsByTagName('object')

        num_objs = len(objects)
        boxes = []
        #TODO add in check for files without xml
        for obj in objects:
            bndbox = obj.childNodes[9]
            xmin = int(bndbox.childNodes[1].firstChild.data)
            xmax = int(bndbox.childNodes[3].firstChild.data)
            ymin = int(bndbox.childNodes[5].firstChild.data)
            ymax = int(bndbox.childNodes[7].firstChild.data)
            boxes.append([xmin, ymin, xmax, ymax])

        # convert everything into a torch.Tensor
        boxes = torch.as_tensor(boxes, dtype=torch.float32)
        # there is only one class
        labels = torch.ones((num_objs,), dtype=torch.int64)

        image_id = torch.tensor([idx])
        area = (boxes[:, 3] - boxes[:, 1]) * (boxes[:, 2] - boxes[:, 0])
        # suppose all instances are not crowd
        iscrowd = torch.zeros((num_objs,), dtype=torch.int64)

        target = {}
        target["boxes"] = boxes
        target["labels"] = labels
        target["image_id"] = image_id
        target["area"] = area
        target["iscrowd"] = iscrowd

        if self.transforms is not None:
            img, target = self.transforms(img, target)

        return img, target

    def __len__(self):
        return len(self.images)
