package com.grsu.map.controller;

import com.grsu.map.domain.Media;
import com.grsu.map.domain.Label;
import com.grsu.map.service.LabelService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MapController {

    private final LabelService labelService;

    public MapController(LabelService labelService) {
        this.labelService = labelService;
    }


    @GetMapping("/map")
    public String getMap(Model model) {
        List<Label> labels = labelService.getLabels();
        model.addAttribute("labels", labels);
        return "map";
    }

    @PostMapping("/add_label")
    public String addLabel(
            @RequestParam(defaultValue = "0") long id,
            @RequestParam String labelName,
            @RequestParam String street,
            @RequestParam String labelContent,
            @RequestParam String labelImage,
            @RequestParam String labelType,
            @RequestParam String coordinates,
            @RequestParam(required = false) String mediaType,
            @RequestParam(required = false) String mediaContent
    ) {
        Label label = labelService.getLabel(id).orElseGet(Label::new);
        label.setName(labelName);
        label.setImage(labelImage);
        label.setDescription(labelContent);
        label.setType(labelType);
        label.setCoordinates(coordinates);
        label.setStreet(street);

        Media media = new Media();
        media.setType(mediaType);
        media.setMedia(mediaContent);
        labelService.addLabel(label, media);
        return "redirect:/map";
    }

    @PostMapping("/delete_label")
    public String deleteLabel(@RequestParam long id) {
        labelService.deleteLabel(id);
        return "redirect:/map";
    }

    @PostMapping("/search")
    public String search(@RequestParam String search, @RequestParam String searchType, Model model) {
        List<Label> result = labelService.searchLabel(search, searchType);
        model.addAttribute("labels", result);
        return "map";
    }
}
