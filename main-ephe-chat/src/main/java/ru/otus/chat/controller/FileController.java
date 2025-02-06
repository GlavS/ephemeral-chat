package ru.otus.chat.controller;

import static org.apache.commons.io.IOUtils.copy;

import jakarta.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
public class FileController {

    @Value("${chat.upload-path}")
    private String uploadPathString;

    @PostMapping("/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String filename = file.getOriginalFilename();
        log.info("Try to upload file: {}", filename);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, calculateDestinationPath(filename), StandardCopyOption.REPLACE_EXISTING);
            log.info("file successfully uploaded");
        } catch (IOException e) {
            throw new UploadException("Failed to store file: " + file.getOriginalFilename(), e);
        }
        return new ResponseEntity<>(filename, HttpStatus.OK);
    }

    @GetMapping("/file/{filename}")
    public void getFile(@PathVariable("filename") String filename, HttpServletResponse response) {
        log.info("Try to read file: {}", filename);

        try (InputStream inputStream =
                new FileInputStream(calculateDestinationPath(filename).toFile())) {
            response.addHeader(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            log.info("file successfully read: {}", filename);
        } catch (Exception e) {
            throw new UploadException("Failed to read file: " + filename, e);
        }
    }

    private Path calculateDestinationPath(String filename) {
        Path rootLocation = Paths.get(uploadPathString);
        Path destinationPath;
        destinationPath = rootLocation
                .resolve(Paths.get(Objects.requireNonNull(filename)))
                .normalize()
                .toAbsolutePath();
        if (!destinationPath.getParent().equals(rootLocation.toAbsolutePath())) {
            // This is a security check
            throw new UploadException("Cannot store file outside current directory.");
        }
        return destinationPath;
    }
}
