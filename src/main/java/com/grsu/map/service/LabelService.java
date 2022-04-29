package com.grsu.map.service;

import com.grsu.map.domain.Label;
import com.grsu.map.repository.LabelRepository;
import com.grsu.map.repository.MediaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
        labelRepository.deleteById(id);
    }

    public List<Label> searchLabel(String search, String searchType) {
        return labelRepository.getLabels(search, searchType);
    }

    public Map<String, List<Label>> getLabelsByStreet(List<Label> labels) {
        return labels.stream().collect(Collectors.groupingBy((Label::getStreet)));
    }
}
