import cv2
import numpy as np
import math
from os import listdir, makedirs
from os.path import exists
from matplotlib import pyplot as plt

image = cv2.imread('20200710034424.jpg')
image = cv2.resize(image,(int(image.shape[1]*0.5),int(image.shape[0]*0.5)))

cv2.imshow('original image',image)

imageGRAY = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
# image = cv2.erode(image,(3,3))
# image = cv2.dilate(image,(3,3))
# image = cv2.blur(image,(3,3))
imageTHRESHED = cv2.adaptiveThreshold(imageGRAY, 255, \
							  cv2.ADAPTIVE_THRESH_MEAN_C,\
                              cv2.THRESH_BINARY, 17,-5)
cv2.imshow('threshed image', imageTHRESHED)

contours = cv2.findContours(imageTHRESHED, cv2.RETR_TREE,\
							cv2.CHAIN_APPROX_SIMPLE)[0]

print("type of contours is: ", type(contours[0]))
# print(contours)

cv2.drawContours(image, contours, -1, (0,0,255),1)

cv2.imshow('image with contours', image)

# try Canny
edges = cv2.Canny(image,100,200)
print("type of edges is: ", type(edges))
plt.subplot(122),plt.imshow(edges,cmap = 'gray')
plt.title('Edge Image'), plt.xticks([]), plt.yticks([])

plt.show()

# try connected Components
num_labels, labels_im = cv2.connectedComponents(imageTHRESHED)



def imshow_components(labels):
	label_hue = np.uint8(179*labels/np.max(labels))
    blank_ch = 255*np.ones_like(label_hue)
    labeled_img = cv2.merge([label_hue, blank_ch, blank_ch])

    # cvt to BGR for display
    labeled_img = cv2.cvtColor(labeled_img, cv2.COLOR_HSV2BGR)

    # set bg label to black
    labeled_img[label_hue==0] = 0

    cv2.imshow('labeled', labeled_img)

imshow_components(labels_im)
