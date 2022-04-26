package com.grsu.map.service;

import com.grsu.map.domain.Label;
import com.grsu.map.domain.Media;
import com.grsu.map.repository.LabelRepository;
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
    private final LabelRepository labelRepository;

    public MediaService(MediaRepository mediaRepository, LabelRepository labelRepository) {
        this.mediaRepository = mediaRepository;
        this.labelRepository = labelRepository;
    }

    public String uploadMedia(MultipartFile file) {
        File uploadFolder = new File(uploadPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdir();
        }

        try {
            file.transferTo(new File(uploadPath + "/" + file.getOriginalFilename()));
        } catch (IOException ignored) {
            //TODO
        }

        return file.getOriginalFilename();
    }

    public void addMedia(MultipartFile file, String type, Label label) {
        Media media = new Media();
        media.setFileName(uploadMedia(file));
        media.setType(type);
        label.getMedia().add(media);
        labelRepository.save(label);
    }

    public void deleteMedia(long id) {
        Media media = mediaRepository.getById(id);
        if (new File(uploadPath + "/" + media.getFileName()).delete()) {
            mediaRepository.deleteById(id);
        }
    }
}
