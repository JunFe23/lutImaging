import cv2
import numpy as np
import sys

img_path, lut_path, output_path = sys.argv[1], sys.argv[2], sys.argv[3]

if 'gray' in lut_path:
    gray = cv2.imread(img_path, cv2.IMREAD_GRAYSCALE)
    result = cv2.cvtColor(gray, cv2.COLOR_GRAY2BGR)
else:
    img = cv2.imread(img_path)
    lut = np.load(lut_path).astype(np.uint8)
    result = cv2.LUT(img, lut)

cv2.imwrite(output_path, result)
