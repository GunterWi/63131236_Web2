package com.ngt.cuoiky.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ngt.cuoiky.exceptions.ProductNotFoundException;
import com.ngt.cuoiky.model.Cart;
import com.ngt.cuoiky.model.Product;
import com.ngt.cuoiky.principal.UserPrincipal;
import com.ngt.cuoiky.service.CartService;
import com.ngt.cuoiky.service.OrderService;
import com.ngt.cuoiky.service.ProductService;

@Controller
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/product/detail/{id}")
    public String detailProduct(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes, Model model,
                                @AuthenticationPrincipal UserPrincipal loggedUser) {
        try {
            Product product = productService.getProductById(id);

            if(loggedUser != null) {
                List<Cart> listCarts = cartService.findCartByUser(loggedUser.getId());
                log.info(listCarts.isEmpty() + "");
                boolean isUserBuyProduct = orderService.isUserHasBuyProduct(loggedUser.getId(), id);

                double estimatedTotal = 0;

                for (Cart item : listCarts) {
                    estimatedTotal += item.getSubtotal();

                }

                log.info(estimatedTotal + "");
                model.addAttribute("isUserBuyProduct", isUserBuyProduct);

                model.addAttribute("listCarts", listCarts);
                model.addAttribute("estimatedTotal", estimatedTotal);
            }

            int pageNum = 1;

    
            model.addAttribute("product", product);

            model.addAttribute("currentPage", pageNum);
            return "product/detail_product";
        }
        catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "product/detail_product";

        }
    }
}