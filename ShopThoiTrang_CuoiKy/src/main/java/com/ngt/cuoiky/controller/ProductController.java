package com.ngt.cuoiky.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/admin/product")
    public String listFirstPage() {
        return "redirect:/admin/product/page/1";
    }
    @GetMapping("/admin/product/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, Model model,
                             @RequestParam(defaultValue = "") String keyword,
                             @RequestParam(defaultValue = "id") String sortField,
                             @RequestParam(required = false) String sortDir) {

        model.addAttribute("sortField", sortField);

        if(sortDir == null) sortDir = "asc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);

        model.addAttribute("keyword", keyword);


        Page<Product> page = productService.listByPage(pageNum, keyword, sortField, sortDir);
        List<Product> listProducts = page.getContent();
        long startCount = (pageNum - 1) * productService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount +  productService.PRODUCT_PER_PAGE - 1;

        if(endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }



        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);

        model.addAttribute("listProducts", listProducts);


        return "product/products";
    }


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
