import torch, os
from PIL import Image
import torchvision.transforms as transforms
from torch.autograd import Variable

articles_to_num = {'athletic-shorts':1, 'button-down':2, 'dress':3, 'hoodie':4, 'jeans':5, 'leggings':6, 'longsleeve':7,
                  'pants':8, 'skirt':9, 'shorts':10, 'tshirt':11, 'sweatshirt':12}

def image_loader(image_name):
    """load image, returns cuda tensor"""
    image = Image.open(image_name)
    image = loader(image).float()
    image = Variable(image, requires_grad=True)
    image = image.unsqueeze(0)  # this is for VGG, may not be needed for ResNet
    return image.cuda()  # assumes that you're using GPU

curDir = os.path.dirname(os.path.realpath(__file__))
device = torch.device('cuda') if torch.cuda.is_available() else torch.device('cpu')
model = torch.load(os.path.join(os.path.dirname(os.path.realpath(__file__)), "model.pth"))

photos = os.listdir(os.path.join(curDir, "stylish"))
for photo in photos:
    file_path = curDir + "\\stylish\\" + photo
    image = Image.open(file_path).convert("RGB")
    imsize = 256
    loader = transforms.Compose([transforms.Scale(imsize), transforms.ToTensor()])
    image = image_loader(file_path)
    pred = model(image)
    print(file_path)
    print(pred)