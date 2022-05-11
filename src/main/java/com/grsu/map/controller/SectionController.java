package com.grsu.map.controller;

import com.grsu.map.domain.Label;
import com.grsu.map.domain.Type;
import com.grsu.map.service.LabelService;
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

    public SectionController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping("/biography")
    public String getBiographies(Model model) {
        Map<String, List<Label>> labelsByStreet = labelService
                .getLabelsByStreet(labelService.searchLabels("", Type.BIOGRAPHY));
        model.addAttribute("labelsByStreet", labelsByStreet);
        return "biography";
    }

    @GetMapping("/photo")
    public String getPhotos(Model model) {
        Map<String, List<Label>> labelsByStreet = labelService
                .getLabelsByStreet(labelService.searchLabels("", Type.PHOTO));
        model.addAttribute("labelsByStreet", labelsByStreet);
        return "photo";
    }

    @GetMapping("/video")
    public String getVideos(Model model) {
        Map<String, List<Label>> labelsByStreet = labelService
                .getLabelsByStreet(labelService.searchLabels("", Type.VIDEO));
        model.addAttribute("labelsByStreet", labelsByStreet);
        return "video";
    }

    @PostMapping("/search_biography")
    public String searchBiography(@RequestParam String search, Model model) {
        Map<String, List<Label>> result = labelService
                .getLabelsByStreet(labelService.searchLabels(search, Type.BIOGRAPHY));
        model.addAttribute("labelsByStreet", result);
        model.addAttribute("search", search);
        return "biography";
    }

    @PostMapping("/search_photo")
    public String searchPhoto(@RequestParam String search, Model model) {
        Map<String, List<Label>> result = labelService
                .getLabelsByStreet(labelService.searchLabels(search, Type.PHOTO));
        model.addAttribute("labelsByStreet", result);
        model.addAttribute("search", search);
        return "photo";
    }

    @PostMapping("/search_video")
    public String searchVideo(@RequestParam String search, Model model) {
        Map<String, List<Label>> result = labelService
                .getLabelsByStreet(labelService.searchLabels(search, Type.VIDEO));
        model.addAttribute("labelsByStreet", result);
        model.addAttribute("search", search);
        return "video";
    }

    @GetMapping("/streets")
    public String getStreets(Model model) {
        List<String> result = labelService.getStreets("");
        model.addAttribute("streets", result);
        return "streets";
    }

    @PostMapping("/search_street")
    public String searchStreet(@RequestParam String search, Model model) {
        List<String> result = labelService.getStreets(search);
        model.addAttribute("streets", result);
        model.addAttribute("search", search);
        return "streets";
    }
}
