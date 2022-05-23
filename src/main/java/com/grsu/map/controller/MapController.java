package com.grsu.map.controller;

import com.grsu.map.domain.Label;
import com.grsu.map.domain.Type;
import com.grsu.map.service.LabelService;
import com.grsu.map.service.MediaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Controller
public class MapController {

    private final LabelService labelService;
    private final MediaService mediaService;
    private final static String HISTORY_ICON = "historyIcon.png";
    private final static String BIOGRAPHY_ICON = "biographyIcon.png";
    private final static String PHOTO_ICON = "photoIcon.png";
    private final static String VIDEO_ICON = "videoIcon.png";
    private final static String OBJECT_ICON = "objectIcon.png";
    private final static List<String> defaultIcons = Arrays.asList(
            HISTORY_ICON,
            BIOGRAPHY_ICON,
            PHOTO_ICON,
            VIDEO_ICON,
            OBJECT_ICON
    );


    public MapController(LabelService labelService, MediaService mediaService) {
        this.labelService = labelService;
        this.mediaService = mediaService;
    }

    @GetMapping("/")
    public String toStart() {
        return "redirect:/start";
    }

    @GetMapping("/start")
    public String start() {
        return "start";
    }

    @GetMapping("/map")
    public String getMap(Model model, Authentication authentication, String showStreet) {
        List<Label> labels = labelService.getLabels();
        //model.addAttribute("isAuthenticated", authentication != null && authentication.isAuthenticated());
        model.addAttribute("isAuthenticated", true);
        model.addAttribute("labels", labels);

        if (showStreet != null) {
            model.addAttribute("showStreet", showStreet);
        }

        return "map";
    }

    @PostMapping("/add_label")
    public String addLabel(
            @RequestParam(defaultValue = "0") long id,
            @RequestParam String labelName,
            @RequestParam String street,
            @RequestParam String labelContent,
            @RequestParam(required = false) MultipartFile labelImage,
            @RequestParam Type labelType,
            @RequestParam String coordinates,
            @RequestParam(required = false) List<MultipartFile> mediaContent
    ) {
        Label label = labelService.getLabel(id).orElseGet(Label::new);

        if (labelImage.isEmpty()) {
            if (label.getImage() == null || defaultIcons.stream().anyMatch(i -> i.equals(label.getImage()))) {
                switch (labelType) {
                    case HISTORY:
                        label.setImage(HISTORY_ICON);
                        break;
                    case BIOGRAPHY:
                        label.setImage(BIOGRAPHY_ICON);
                        break;
                    case PHOTO:
                        label.setImage(PHOTO_ICON);
                        break;
                    case VIDEO:
                        label.setImage(VIDEO_ICON);
                        break;
                    case OBJECT:
                        label.setImage(OBJECT_ICON);
                        break;
                }
            }
        } else {
            label.setImage(mediaService.uploadMedia(labelImage));
        }

        label.setName(labelName);
        label.setDescription(labelContent);
        label.setType(labelType);
        label.setCoordinates(coordinates);
        label.setStreet(street);

        if (!mediaContent.isEmpty()) {
            mediaContent.forEach(file -> mediaService.addMedia(file, label));
        }

        labelService.addLabel(label);

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
    public String search(@RequestParam String search, @RequestParam Type searchType, Model model) {
        List<Label> result = labelService.searchLabels(search, searchType);
        model.addAttribute("labels", result);
        return "map";
    }

    @PostMapping("/delete_label_image")
    public String deleteLabelImage(@RequestParam long id) {
        Label label = labelService.getLabel(id).orElseGet(Label::new);

        if (defaultIcons.stream().noneMatch(icon -> icon.equals(label.getImage()))) {
            switch (label.getType()) {
                case HISTORY:
                    if (mediaService.deleteFile(label.getImage())) {
                        label.setImage(HISTORY_ICON);
                    }
                    break;
                case BIOGRAPHY:
                    if (mediaService.deleteFile(label.getImage())) {
                        label.setImage(BIOGRAPHY_ICON);
                    }

                    break;
                case PHOTO:
                    if (mediaService.deleteFile(label.getImage())) {
                        label.setImage(PHOTO_ICON);
                    }

                    break;
                case VIDEO:
                    if (mediaService.deleteFile(label.getImage())) {
                        label.setImage(VIDEO_ICON);
                    }

                    break;
                case OBJECT:
                    if (mediaService.deleteFile(label.getImage())) {
                        label.setImage(OBJECT_ICON);
                    }

                    break;
            }
        }

        labelService.addLabel(label);
        return "redirect:/map";
    }
}
