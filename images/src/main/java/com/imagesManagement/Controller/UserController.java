package com.imagesManagement.Controller;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.imagesManagement.Model.Image;
import com.imagesManagement.Service.ImageService;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final ImageService imageService;

	@GetMapping("/login")
	public String login() {
		if (isAuthenticated()) {
			return "redirect:/";
		}
		return "login";
	}

	@GetMapping("/")
	public String landingPage(Model map) {
		List<Image> images = imageService.getAcceptedImages();
		map.addAttribute("images", images);
		return "acceptedImages";
	}

	@GetMapping("/admin")
	public String adminPage(Model map) {
		List<Image> images = imageService.getNonProcessedImages();
		map.addAttribute("images", images);
		return "nonProcessedImages";
	}

	private boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
			return false;
		}
		return authentication.isAuthenticated();
	}

	@GetMapping("/denied")
	public String deniedPage() {
		return "denied";
	}

}
