package com.imagesManagement.Controller;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.imagesManagement.Model.User;
import com.imagesManagement.Service.UserService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserRegistration {

	private final UserService userService;

	@GetMapping("/")
	public String goToRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "createUser";
	}

	@PostMapping("/register")
	public String Register(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			return "redirect:/user/?invalidPattern";
		}
		
		User existUser = userService.findUserByEmail(user.getEmail());
		if (existUser != null) {
			return "redirect:/user/?error";
		}
		
		userService.save(user);
		return "redirect:/user/?success";
	}

}
