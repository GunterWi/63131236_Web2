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

    @GetMapping("/admin/order/page/{pageNum}")
    public String listOrderAdminPage(Model model){
        // giả định
        model.addAttribute("keyword", keyword);

        model.addAttribute("orderStatusList", orderStatusList);
        model.addAttribute("user", user);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("listOrders", listOrders);
        model.addAttribute("status", status);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        return "order/orders";
    }
}
