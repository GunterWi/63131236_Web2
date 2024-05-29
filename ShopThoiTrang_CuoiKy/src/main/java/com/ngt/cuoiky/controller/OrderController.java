package com.ngt.cuoiky.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ngt.cuoiky.principal.UserPrincipal;

@Controller
public class OrderController {
    
    @GetMapping("/admin/order")
    public String listOrderAdminFirstPage() {
        return "redirect:/admin/order/page/1";
    }
}
