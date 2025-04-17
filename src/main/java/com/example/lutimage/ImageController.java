package com.example.lutimage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ImageController {
    @PostMapping("/apply-lut")
    public ResponseEntity<?> applyLUT(@RequestParam("file") MultipartFile file,
                                      @RequestParam("filter") String filterName) {
        try {
            // 1. 파일명 UUID 생성
            String uuid = UUID.randomUUID().toString();
            String inputPath = "upload/" + uuid + ".jpg";
            String outputPath = "download/" + uuid + ".jpg";

            // 2. 필터 LUT 경로 매핑
            String lutPath = switch (filterName) {
                case "warm" -> "lut/warm.npy";
                case "cool" -> "lut/cool.npy";
                case "gray" -> "lut/gray.npy";
                case "bright" -> "lut/bright.npy";
                case "dark" -> "lut/dark.npy";
                default -> "lut/warm.npy"; // fallback
            };

            // 3. 업로드 파일 저장
            Files.copy(file.getInputStream(), new File(inputPath).toPath());

            // 4. Python 스크립트 실행
            ProcessBuilder pb = new ProcessBuilder("python3", "python/apply_lut.py",
                    inputPath, lutPath, outputPath);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // 5. 로그 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder outputLog = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                outputLog.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return ResponseEntity.ok("/download/" + uuid + ".jpg");
            } else {
                return ResponseEntity.status(500).body("처리 실패\n" + outputLog);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류: " + e.getMessage());
        }
    }

    @GetMapping("/album-list")
    public ResponseEntity<List<String>> getAlbumImages() {
        File downloadDir = new File("download");
        String[] fileList = downloadDir.list((dir, name) ->
                name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

        if (fileList == null) return ResponseEntity.ok(Collections.emptyList());

        List<String> imagePaths = Arrays.stream(fileList)
                .map(name -> "/download/" + name)
                .sorted(Comparator.reverseOrder()) // 최신순
                .collect(Collectors.toList());

        return ResponseEntity.ok(imagePaths);
    }
}
