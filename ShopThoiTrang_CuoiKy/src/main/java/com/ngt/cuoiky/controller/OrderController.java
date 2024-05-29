package com.ngt.cuoiky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ngt.cuoiky.model.Order;
import com.ngt.cuoiky.model.OrderStatus;
import com.ngt.cuoiky.principal.UserPrincipal;
import com.ngt.cuoiky.service.OrderService;
import com.ngt.cuoiky.service.OrderStatusService;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderStatusService orderStatusService;

    @GetMapping("/admin/order")
    public String listOrderAdminFirstPage() {
        return "redirect:/admin/order/page/1";
    }

    @GetMapping("/admin/order/page/{pageNum}")
    public String listOrderAdminPage(Model model,
                                     @PathVariable(name = "pageNum") Integer pageNum,
                                     @RequestParam(name = "keyword", required = false) String keyword,@RequestParam(name = "status", required = false) String status){
        try {
            if(status == null) {
                status = "ALL";
            }
            
            Page<Order> page = orderService.listByPage(pageNum, keyword, status);
            List<Order> listOrders = page.getContent();


            long startCount = (long) (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
            long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
            if (endCount > page.getTotalElements()) {
                endCount = page.getTotalElements();
            }
            List<OrderStatus> orderStatusList = orderStatusService.listOrderStatus();
            model.addAttribute("keyword", keyword);

            model.addAttribute("orderStatusList", orderStatusList);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("listOrders", listOrders);
            model.addAttribute("status", status);
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
        }
        catch (Exception e) {
            status = "ALL";
        }
        
        return "order/orders";
    }
}
