package com.grsu.map.service;

import com.grsu.map.domain.Label;
import com.grsu.map.domain.Type;
import com.grsu.map.repository.LabelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;

@Service
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
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

    public List<Label> searchLabels(String search, Type searchType) {
        return labelRepository.getLabels(search, searchType);
    }

    public Map<String, List<Label>> getLabelsByStreet(List<Label> labels) {
        return labels.stream().collect(groupingBy((Label::getStreet)));
    }

    public List<String> getStreets(String search) {
        return labelRepository.getStreets(search);
    }
}
