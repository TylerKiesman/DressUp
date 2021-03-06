import torch, os
from models.detection.engine import train_one_epoch, evaluate
from models.clothingDataset import ClothingDataset
from models.customModel import *
from models.detection import utils
from PIL import Image
import torchvision.transforms as transforms
from torch.autograd import Variable


def main():
    # train on the GPU or on the CPU, if a GPU is not available
    device = torch.device('cuda') if torch.cuda.is_available() else torch.device('cpu')
    curDir = os.path.dirname(os.path.realpath(__file__))

    # our dataset has two classes only - background and person
    num_classes = 20
    model = get_model_instance_segmentation(num_classes)
    # use our dataset and defined transformations
    dataset = ClothingDataset(curDir, get_transform(train=True))
    dataset_test = ClothingDataset(curDir, get_transform(train=False))

    # split the dataset in train and test set
    indices = torch.randperm(len(dataset)).tolist()
    split = round(len(indices) / 2)
    dataset = torch.utils.data.Subset(dataset, indices[:-split])
    dataset_test = torch.utils.data.Subset(dataset_test, indices[-split:])

    # define training and validation data loaders
    data_loader = torch.utils.data.DataLoader(
        dataset, batch_size=1, shuffle=True, num_workers=4,
        collate_fn=utils.collate_fn)

    data_loader_test = torch.utils.data.DataLoader(
        dataset_test, batch_size=1, shuffle=False, num_workers=4,
        collate_fn=utils.collate_fn)

    # move model to the right device
    model.to(device)

    # construct an optimizer
    params = [p for p in model.parameters() if p.requires_grad]
    optimizer = torch.optim.SGD(params, lr=0.00005,
                                momentum=0.9, weight_decay=0.0005)
    # and a learning rate scheduler
    lr_scheduler = torch.optim.lr_scheduler.StepLR(optimizer,
                                                   step_size=2,
                                                   gamma=0.1)

    # let's train it for 5 epochs
    num_epochs = 5

    for epoch in range(num_epochs):
        # train for one epoch, printing every 10 iterations
        train_one_epoch(model, optimizer, data_loader, device, epoch, print_freq=10)
        # update the learning rate
        lr_scheduler.step()
        # evaluate on the test dataset
        evaluate(model, data_loader_test, device=device)
    torch.save(model, os.path.join(curDir, "model.pth"))
    print("Done training.")


if __name__ == '__main__':
    main()