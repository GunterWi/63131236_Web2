package com.ngt.cuoiky.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ngt.cuoiky.model.Cart;
import com.ngt.cuoiky.model.Order;
import com.ngt.cuoiky.model.OrderStatus;
import com.ngt.cuoiky.model.User;
import com.ngt.cuoiky.principal.UserPrincipal;
import com.ngt.cuoiky.service.CartService;
import com.ngt.cuoiky.service.OrderService;
import com.ngt.cuoiky.service.OrderStatusService;
import com.ngt.cuoiky.service.UserService;

@Controller
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @GetMapping("/profile/order/info")
    public String listOrderFirstPage() {
        return "redirect:/profile/order/info/page/1";
    }

    @GetMapping("/profile/order/info/page/{pageNum}")
    public String profileOrderInfo(@AuthenticationPrincipal UserPrincipal loggedUser, Model model,
                                   @PathVariable(name = "pageNum") Integer pageNum,
                                   @RequestParam(name = "keyword", required = false) String keyword,
                                   @RequestParam(name = "status", required = false) Integer status,
                                   RedirectAttributes redirectAttributes) {
        Integer id = loggedUser.getId();
        try {
            log.info("Start profile order!!!!");
            User user = userService.getUserByID(id);
            log.info(user.toString());
            List<Cart> listCarts = cartService.findCartByUser(loggedUser.getId());
            double estimatedTotal = 0;

            for (Cart item : listCarts) {
                estimatedTotal += item.getSubtotal();

            }

            if(status == null) {
                status = 0; // status all
            }


            Page<Order> page = orderService.listForUserByPage(user, pageNum, keyword, status);
            List<Order> listOrders = page.getContent();


            long startCount = (long) (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
            long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
            if (endCount > page.getTotalElements()) {
                endCount = page.getTotalElements();
            }
            List<OrderStatus> orderStatusList = orderStatusService.listOrderStatus();

            log.info("user has add to model");
            model.addAttribute("listCarts", listCarts);
            model.addAttribute("estimatedTotal", estimatedTotal);
            model.addAttribute("orderStatusList", orderStatusList);
            model.addAttribute("user", user);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("listOrders", listOrders);
            model.addAttribute("keyword", keyword);
            model.addAttribute("status", status);
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);

            return "profile-user/order-info";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/";
        }

    }

    @GetMapping("/profile/order/info/detail/{id}")
    public String detailProfileUser(@AuthenticationPrincipal UserPrincipal loggedUser, Model model,
                                    @PathVariable(name = "id") Integer orderId,
                                    RedirectAttributes redirectAttributes) {

        Integer id = loggedUser.getId();
        try {
            log.info("Start detail order!!!!");
            User user = userService.getUserByID(id);
            Order order = orderService.getOrder(orderId, user);
            model.addAttribute("order", order);
            return "profile-user/order-details-modal";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/";
        }
    }


    @GetMapping("/admin/order")
    public String listOrderAdminFirstPage() {
        return "redirect:/admin/order/page/1";
    }

    @GetMapping("/admin/order/page/{pageNum}")
    public String listOrderAdminPage(Model model,
                                     @PathVariable(name = "pageNum") Integer pageNum,
                                     @RequestParam(name = "keyword", required = false) String keyword,@RequestParam(name = "status", required = false) Integer status){
        try {
            if(status == null) {
                status = 0; // status all
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
            status = 0;
        }
        
        return "order/orders";
    }

    @GetMapping("/admin/order/accept")
    public String acceptOrder(@RequestParam("id") Integer id, @RequestParam("statusId") Integer statusId) throws Exception {

        orderService.acceptOrder(id, statusId);
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/deny")
    public String denyOrder(@RequestParam("id") Integer id, @RequestParam("statusId") Integer statusId) throws Exception {

        orderService.denyOrder(id, statusId);
        return "redirect:/admin/order";
    }

    @GetMapping("/profile/order/requestCancel")
    public String requestCancel(@RequestParam("id") Integer id) throws Exception {
        orderService.requestCancel(id);
        return "redirect:/profile/order/info";
    }

    @GetMapping("/profile/order/cancelRequest")
    public String cancelRequest(@RequestParam("id") Integer id) throws Exception {
        orderService.cancelRequest(id);
        return "redirect:/profile/order/info";
    }
}
