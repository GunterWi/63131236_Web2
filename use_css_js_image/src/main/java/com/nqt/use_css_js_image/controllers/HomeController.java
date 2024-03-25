package com.nqt.use_css_js_image.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
@GetMapping("/")
	public String Index() {
		return "index";
	}
}
