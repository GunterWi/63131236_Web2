package com.ngt.cuoiky.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ngt.cuoiky.model.Brand;
import com.ngt.cuoiky.model.Cart;
import com.ngt.cuoiky.model.Category;
import com.ngt.cuoiky.model.Poster;
import com.ngt.cuoiky.model.Product;
import com.ngt.cuoiky.principal.UserPrincipal;
import com.ngt.cuoiky.service.BrandService;
import com.ngt.cuoiky.service.CartService;
import com.ngt.cuoiky.service.CategoryService;
import com.ngt.cuoiky.service.PosterService;
import com.ngt.cuoiky.service.ProductService;

import org.springframework.ui.Model;

@Controller
public class MainController {
    private final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private PosterService posterService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserPrincipal loggedUser) {
        Page<Product> page = productService.listLatestProduct();
        Page<Product> pageBestSell = productService.listBestSellProduct();

        if(loggedUser != null) {
            List<Cart> listCarts = cartService.findCartByUser(loggedUser.getId());
            log.info(listCarts.isEmpty() + "");

            double estimatedTotal = 0;

            for (Cart item : listCarts) {
                estimatedTotal += item.getSubtotal();

            }

            log.info(estimatedTotal + "");
            model.addAttribute("listCarts", listCarts);
            model.addAttribute("estimatedTotal", estimatedTotal);
        }

        List<Brand> listBrands = brandService.getAllBrand();
        List<Category> listCategories = categoryService.findAllCategory();
        List<Poster> listPostersLeft = posterService.listPosterLeftUser();
        List<Poster> listPostersRight = posterService.listPosterRightUser();
        List<Product> listLatestProducts = page.getContent();
        List<Product> listBestSellProducts = pageBestSell.getContent();

        model.addAttribute("listPostersLeft", listPostersLeft);
        model.addAttribute("listPostersRight", listPostersRight);
        model.addAttribute("listLatestProducts", listLatestProducts);
        model.addAttribute("listBestSellProducts", listBestSellProducts);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("listCategories", listCategories);
        return "index";
    }

}
