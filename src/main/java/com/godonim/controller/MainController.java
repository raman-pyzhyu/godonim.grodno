package com.godonim.controller;

import com.godonim.repository.LabelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

	private final LabelRepo labelRepo;

	@GetMapping("/map")
	public String getMap(Model model) {
		model.addAttribute("labels", labelRepo.findAll());
		return "map";
	}
}
