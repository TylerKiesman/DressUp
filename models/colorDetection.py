from sklearn.cluster import KMeans
import numpy as np
import cv2

"""
Function that given a file path and coordinates for an object, will tell the color of it.
x1, y1 is top left and x2, y2 is bottom right
"""
def findColor(imgPath, x1, y1, x2, y2):
    image = cv2.imread(imgPath)
    image = image[y1:y2, x1:x2]
    image = image.reshape((image.shape[0] * image.shape[1], 3))
    clt = KMeans(n_clusters=3)
    clt.fit(image)
    numLabels = np.arange(0, len(np.unique(clt.labels_)) + 1)
    (hist, _) = np.histogram(clt.labels_, bins=numLabels)
    # normalize the histogram, such that it sums to one
    hist = hist.astype("float")
    hist /= hist.sum()
    return zip(hist, clt.cluster_centers_)