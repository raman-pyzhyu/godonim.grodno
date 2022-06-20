package com.grsu.map.controller;

import com.grsu.map.domain.Label;
import com.grsu.map.domain.Media;
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
import java.util.stream.Collectors;

@Controller
public class MapController {

    private final LabelService labelService;
    private final MediaService mediaService;
    private final static String HISTORY_ICON = "history.png";
    private final static String BIOGRAPHY_ICON = "biography.png";
    private final static String PHOTO_ICON = "photo.png";
    private final static String VIDEO_ICON = "video.png";
    private final static String OBJECT_ICON = "object.png";
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
        model.addAttribute("searchType", Type.UNKNOWN);

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
        Label label = labelService.getLabel(id).orElseGet(Label::new);
        label.getMedia()
                .forEach(media -> mediaService.deleteMedia(media.getId()));
        labelService.deleteLabel(id);
        return "redirect:/map";
    }

    @PostMapping("/edit_media_description")
    public String editMediaDescription(@RequestParam(value = "id") List<Long> ids,
                                       @RequestParam(value = "description") List<String> descriptions,
                                       @RequestParam(value = "deleteId", required = false) List<Long> deleteIds) {
        Media media;

        if (deleteIds != null) {
            deleteIds.forEach(mediaService::deleteMedia);
            ids = ids.stream().distinct().collect(Collectors.toList());
        }

        for (int i = 0; i < ids.size(); i++) {
            media = mediaService.getMedia(ids.get(i)).orElseGet(Media::new);
            media.setDescription(descriptions.size() != 0 ? descriptions.get(i) : "");
            mediaService.saveMedia(media);
        }

        return "redirect:/map";
    }

    @PostMapping("/search")
    public String search(@RequestParam String search, @RequestParam Type searchType, Model model) {
        List<Label> result = labelService.searchLabels(search, searchType);
        model.addAttribute("search", search);
        model.addAttribute("searchType", searchType);
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
