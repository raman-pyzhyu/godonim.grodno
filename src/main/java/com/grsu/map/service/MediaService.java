package com.grsu.map.service;

import com.grsu.map.domain.Label;
import com.grsu.map.domain.Media;
import com.grsu.map.repository.LabelRepository;
import com.grsu.map.repository.MediaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
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

        File uploadFile = new File(uploadPath + "/" + file.getOriginalFilename());

        if (!uploadFile.exists()) {
            try {
                file.transferTo(uploadFile);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

            return file.getOriginalFilename();
        }

        return "";
    }

    public void addMedia(MultipartFile file, Label label) {
        Media media = new Media();
        String upload;

        if (!(upload = uploadMedia(file)).equals("")) {
            media.setFileName(upload);
            label.getMedia().add(media);
            labelRepository.save(label);
        }
    }

    public void deleteMedia(long id) {
        Media media = mediaRepository.findById(id).orElseGet(Media::new);

        if (deleteFile(media.getFileName())) {
            mediaRepository.deleteById(id);
        }
    }

    public boolean deleteFile(String fileName) {
        return new File(uploadPath + "/" + fileName).delete();
    }
}
