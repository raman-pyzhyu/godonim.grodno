package com.grsu.map.controller;

import com.grsu.map.domain.Label;
import com.grsu.map.service.LabelService;
import com.grsu.map.service.MediaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class SectionController {

    private final LabelService labelService;
    private final MediaService mediaService;

    public SectionController(LabelService labelService, MediaService mediaService) {
        this.labelService = labelService;
        this.mediaService = mediaService;
    }


    @GetMapping("/biography")
    public String getBiographies(Model model) {
        List<Label> labels = labelService.searchLabel("", "2");
        model.addAttribute("labels", labels);
        return "biography";
    }

    @GetMapping("/photo")
    public String getPhotos(Model model) {
        Map<String, List<Label>> labelsByStreet = labelService.getLabelsByStreet();
        model.addAttribute("labelsByStreet", labelsByStreet);
        return "photo";
    }

    @GetMapping("/video")
    public String getVideos(Model model) {
        List<Label> labels = labelService.searchLabel("", "4");
        model.addAttribute("labels", labels);
        return "video";
    }

    @PostMapping("/searchBiography")
    public String searchBiography(@RequestParam String search, Model model) {
        List<Label> result = labelService.searchLabel(search, "2");
        model.addAttribute("labels", result);
        model.addAttribute("search", search);
        return "biography";
    }

    @PostMapping("/searchPhoto")
    public String searchPhoto(@RequestParam String search, Model model) {
        List<Label> result = labelService.searchLabel(search, "3");
        model.addAttribute("labels", result);
        return "photo";
    }

    @PostMapping("/searchVideo")
    public String searchVideo(@RequestParam String search, Model model) {
        List<Label> result = labelService.searchLabel(search, "4");
        model.addAttribute("labels", result);
        return "video";
    }
}
