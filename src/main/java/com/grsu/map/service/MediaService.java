package com.grsu.map.service;

import com.grsu.map.domain.Label;
import com.grsu.map.domain.Media;
import com.grsu.map.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class MediaService {

    @Value("${upload.path}")
    private String uploadPath;
    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    private String uploadMedia(MultipartFile file) {
        File uploadFolder = new File(uploadPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdir();
        }
        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();

        try {
            file.transferTo(new File(uploadPath + "/" + resultFilename));
        } catch (IOException e) {

        }

        return resultFilename;
    }

    public void addMedia(MultipartFile file, String type, Label label) {
        Media media = new Media();
        media.setMedia(uploadMedia(file));
        media.setType(type);
        media.setLabel(label);
        mediaRepository.save(media);
    }
}
