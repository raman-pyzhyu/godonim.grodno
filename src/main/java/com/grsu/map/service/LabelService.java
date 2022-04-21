package com.grsu.map.service;

import com.grsu.map.domain.Label;
import com.grsu.map.repository.LabelRepository;
import com.grsu.map.repository.MediaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabelService {

    private final LabelRepository labelRepository;
    private final MediaRepository mediaRepository;

    public LabelService(LabelRepository labelRepository, MediaRepository mediaRepository) {
        this.labelRepository = labelRepository;
        this.mediaRepository = mediaRepository;
    }

    public void addLabel(Label label) {
        labelRepository.save(label);

    }

    public List<Label> getLabels() {
        return labelRepository.findAll();
    }

    public Optional<Label> getLabel(long id) {
        return labelRepository.findById(id);
    }

    public void deleteLabel(long id) {
        try {
            labelRepository.deleteById(id);
        } catch (Exception e) {
            mediaRepository.findAll().forEach(media -> {
                if (media.getLabel().equals(labelRepository.findById(id).orElseGet(Label::new))) {
                    mediaRepository.deleteById(media.getId());
                }
            });
        }
    }

    public List<Label> searchLabel(String search, String searchType) {
        return labelRepository.getLabels(search, searchType);
    }
}
