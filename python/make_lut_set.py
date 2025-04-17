import numpy as np
import os

output_dir = "../lut"
os.makedirs(output_dir, exist_ok=True)

def make_lut(name, func):
    lut = np.zeros((256, 1, 3), dtype=np.uint8)
    for i in range(256):
        lut[i, 0] = func(i)
    np.save(f"{output_dir}/{name}.npy", lut)
    print(f"✅ {name}.npy 생성 완료")

# 필터 5종 생성
make_lut("warm", lambda i: [min(i+30, 255), i, max(i-30, 0)])     # 따뜻한 필터
make_lut("cool", lambda i: [i, max(i-20, 0), min(i+30, 255)])     # 차가운 필터
make_lut("gray", lambda i: [i, i, i])                             # 흑백
make_lut("bright", lambda i: [min(i+50, 255)] * 3)                # 밝게
make_lut("dark", lambda i: [max(i-50, 0)] * 3)                    # 어둡게
