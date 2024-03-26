package thigk.ntu63131236.nguyenquocthai_ktgiuaki.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"/", "/home"})
    public String index() {
        return "index";
    }
}
