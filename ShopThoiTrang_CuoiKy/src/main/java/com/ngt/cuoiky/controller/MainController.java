package com.ngt.cuoiky.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.ngt.cuoiky.model.Brand;
import com.ngt.cuoiky.model.Category;
import com.ngt.cuoiky.model.Poster;
import com.ngt.cuoiky.model.Product;
import com.ngt.cuoiky.service.BrandService;
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

    @GetMapping("/")
    public String index(Model model) {
        Page<Product> page = productService.listLatestProduct();
        Page<Product> pageBestSell = productService.listBestSellProduct();
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