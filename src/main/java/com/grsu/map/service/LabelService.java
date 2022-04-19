package com.grsu.map.service;

import com.grsu.map.domain.Media;
import com.grsu.map.domain.Label;
import com.grsu.map.repository.MediaRepository;
import com.grsu.map.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LabelService {

    private final LabelRepository labelRepository;
    private final MediaRepository mediaRepository;

    public LabelService(LabelRepository labelRepository, MediaRepository mediaRepository) {
        this.labelRepository = labelRepository;
        this.mediaRepository = mediaRepository;
    }

    public void addLabel(Label label, Media media) {
        media.setLabel(label);
        mediaRepository.save(media);
        labelRepository.save(label);

    }

    public List<Label> getLabels() {
        return labelRepository.findAll();
    }

    public Optional<Label> getLabel(long id) {
        return labelRepository.findById(id);
    }

    public void deleteLabel(long id) {
        labelRepository.deleteById(id);
    }

    public List<Label> searchLabel(String search, String searchType) {
        List<Label> labels = getLabels();
        return labels.stream()
                .filter(label -> searchType.equals("") || label.getType().equals(searchType))
                .filter(label -> search.equals("") || (label.getName().toLowerCase().contains(search.toLowerCase())
                        || label.getDescription().toLowerCase().contains(search.toLowerCase())))
                .collect(Collectors.toList());
    }
}
