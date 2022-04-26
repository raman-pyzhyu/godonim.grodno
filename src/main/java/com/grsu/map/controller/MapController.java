package com.grsu.map.controller;

import com.grsu.map.domain.Label;
import com.grsu.map.service.LabelService;
import com.grsu.map.service.MediaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class MapController {

    private final LabelService labelService;
    private final MediaService mediaService;

    public MapController(LabelService labelService, MediaService mediaService) {
        this.labelService = labelService;
        this.mediaService = mediaService;
    }

    @GetMapping("/")
    public String start() {
        return "redirect:/map";
    }

    @GetMapping("/map")
    public String getMap(Model model, Authentication authentication) {
        List<Label> labels = labelService.getLabels();
        //model.addAttribute("isAuthenticated", authentication != null && authentication.isAuthenticated());
        model.addAttribute("isAuthenticated", true);
        model.addAttribute("labels", labels);
        return "map";
    }

    @PostMapping("/add_label")
    public String addLabel(
            @RequestParam(defaultValue = "0") long id,
            @RequestParam String labelName,
            @RequestParam String street,
            @RequestParam String labelContent,
            // @RequestParam(required = false) MultipartFile labelImage,
            @RequestParam String labelType,
            @RequestParam String coordinates,
            @RequestParam(required = false) MultipartFile mediaContent
    ) {
        Label label = labelService.getLabel(id).orElseGet(Label::new);
        label.setName(labelName);
        //label.setImage(labelImage);
        label.setDescription(labelContent);
        label.setType(labelType);
        label.setCoordinates(coordinates);
        label.setStreet(street);

        if (!mediaContent.isEmpty()) {
            mediaService.addMedia(mediaContent, labelType, label);
        } else {
            labelService.addLabel(label);
        }
        return "redirect:/map";
    }

    @PostMapping("/delete_label")
    public String deleteLabel(@RequestParam long id) {
        labelService.getLabel(id).orElseGet(Label::new).getMedia()
                .forEach(media -> mediaService.deleteMedia(media.getId()));
        labelService.deleteLabel(id);
        return "redirect:/map";
    }

    @PostMapping("/delete_media")
    public String deleteMedia(@RequestParam long id) {
        mediaService.deleteMedia(id);
        return "redirect:/map";
    }

    @PostMapping("/search")
    public String search(@RequestParam String search, @RequestParam String searchType, Model model) {
        List<Label> result = labelService.searchLabel(search, searchType);
        model.addAttribute("labels", result);
        return "map";
    }
}
