package com.grsu.map.controller;

import com.grsu.map.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final MediaService mediaService;

    public UploadController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping
    public ResponseEntity<String> upload(@RequestParam(required = false) MultipartFile file) throws IOException {
        String path = mediaService.uploadMedia(file);
        String location = "{ \"location\":\"uploads/" + path + "\"}";
        return ResponseEntity.ok(location);
    }
}
