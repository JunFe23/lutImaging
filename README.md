# lutimage processing module

image를 업로드해서 lutimage필터 적용하여 저장

## 개요
개인과제 선정 중 하나로 만들었지만 채택은 되지 않음. 기록용으로 남김.

## 🛠 기술 스택
### 🔧 Backend: Spring Boot
- **언어**: Java 17
- **프레임워크**: Spring Boot
- **빌드 도구**: Gradle
- **주요 기능**:
    - Multipart 이미지 업로드 처리
    - 사용자 선택 필터에 따라 Python 스크립트 호출 (LUT 필터 적용)
    - 결과 이미지를 UUID 기반 파일명으로 저장
    - `download/` 폴더를 정적 리소스로 서빙
    - `/album-list` API로 저장된 이미지 목록 반환

### 🐍 Image Processing: Python + OpenCV
- **언어**: Python 3.x
- **라이브러리**: OpenCV (`opencv-python`), NumPy
- **주요 스크립트**:
    - `apply_lut.py`: 선택한 LUT 필터 적용 후 결과 이미지 저장
    - `make_lut_set.py`: 5종 LUT 필터(`.npy`) 자동 생성 (warm, cool, gray, bright, dark)
- **기능 특징**:
    - 흑백 필터는 Grayscale 방식으로 별도 처리
    - RGB 각 채널에 LUT를 개별 적용

### 🌐 Frontend: HTML + JavaScript (Vanilla)
- **UI 구성**: 정적 HTML + JS
- **주요 페이지**:
    - `index.html`: 이미지 업로드, 필터 선택, 미리보기
    - `album.html`: 저장된 결과 이미지 목록(앨범) 표시
- **기능 특징**:
    - `<form>` + `fetch()` API를 활용한 비동기 업로드
    - 선택된 필터값 전송 및 처리 결과 미리보기 표시
    - 앨범 목록을 동적으로 렌더링

## ⚙️ 실행 방법
```bash
./gradlew bootRun

## 프로젝트 구조
lutimage/
├── upload/              # 원본 이미지 저장
├── download/            # 필터 적용된 결과 이미지 저장
├── lut/                 # .npy LUT 필터 파일 저장
├── python/              # Python 스크립트 & LUT 생성기
├── src/main/resources/static/
│   ├── index.html       # 메인 화면 (필터 적용)
│   └── album.html       # 앨범 보기 화면
└── src/main/java/...    # Spring Controller
