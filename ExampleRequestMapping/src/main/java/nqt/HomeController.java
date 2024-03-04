package nqt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home/")
public class HomeController {
	@RequestMapping("index")
	public String index() {
		return "home/index";
	}
	@RequestMapping("about")
	public String about() {
		return "home/about";
	}
	@RequestMapping("contact")
	public String contact() {
		return "home/contact";
	}
	@RequestMapping("feedback")
	public String feedback() {
		return "home/feedback";
	}
	@RequestMapping("faq")
	public String faq() {
		return "home/faq";
	}
}
