package com.nqt.HelloWorldSpring.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping("/")
	public String trangchu(ModelMap model) {
		model.addAttribute("tb","Xin chào");
		return "index";
	}
}
