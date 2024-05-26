package com.ngt.cuoiky.controller;

import java.util.List;
import java.util.Date;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ngt.cuoiky.model.Brand;
import com.ngt.cuoiky.model.Cart;
import com.ngt.cuoiky.model.Category;
import com.ngt.cuoiky.model.Product;
import com.ngt.cuoiky.principal.UserPrincipal;
import com.ngt.cuoiky.service.BrandService;
import com.ngt.cuoiky.service.CartService;
import com.ngt.cuoiky.service.CategoryService;
import com.ngt.cuoiky.service.OrderService;
import com.ngt.cuoiky.service.ProductService;
import com.ngt.cuoiky.service.StorageService;

@Controller
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(ProductController.class);


    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StorageService storageService;

    @GetMapping("/admin/product")
    public String listFirstPage() {
        return "redirect:/admin/product/page/1";
    }
    @GetMapping("/admin/product/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, Model model,
                             @RequestParam(defaultValue = "") String keyword) {

        model.addAttribute("keyword", keyword);

        Page<Product> page = productService.listByPage(pageNum, keyword);
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


    @GetMapping("/admin/product/add")
    public String addProduct(Model model) {
        List<Brand> listBrands = brandService.getAllBrand();
        List<Category> listCategories = categoryService.findAllCategory();

        Product product = new Product();
        product.setIsActive(true);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("listCategories", listCategories);
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", product);
        }

        return "product/new_product";
    }

    @PostMapping("/admin/product/add")
    public String saveProduct(Product product, BindingResult errors, RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file) throws Exception, IOException {

        if(product.getName().trim().length() == 0) {
            errors.rejectValue("name", "product", "Vui lòng nhập tên sản phẩm !");
        }
        if(productService.getProductByName(product.getName()) != null) {
            errors.rejectValue("name", "product", "Tên sản phẩm không được trùng!");
        }
        if(product.getPrice() == null) {
            errors.rejectValue("price", "product", "Vui lòng nhập đơn giá !");
        }
        else if(product.getPrice() <= 0) {
            errors.rejectValue("price", "product", "Đơn giá phải lớn hơn 0 !");
        }
        if(product.getShortDescription().trim().length() == 0) {
            errors.rejectValue("shortDescription", "product", "Short description không được bỏ trống!");
        }
        if(product.getDescription().trim().length() == 0) {
            errors.rejectValue("description", "product", "Description không được bỏ trống!");
        }
        if(product.getDiscount() == null) {
            product.setDiscount(0);
        }
        if(product.getDiscount()  < 0 || product.getDiscount() > 100 ) {
            errors.rejectValue("discount", "product", "Discount  phải lon hon hoac bang 0 va be hon hoac bang 100!");
        }
        if(product.getInStock() == null) {
            errors.rejectValue("inStock", "product", "In stock không được bỏ trống!");
        }
        else if(product.getInStock() < 0) {
            errors.rejectValue("inStock", "product", "Số lượng phải nhiều hơn hoac bang 0 !");
        }

        if(product.getImage() == null) {
            errors.rejectValue("image", "product", "Image không được bỏ trống!");
        }

        if(errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.product", errors);
            redirectAttributes.addFlashAttribute("product", product);
            return "redirect:/admin/product/add";
        }

        else {
              String url = storageService.upload(file);
              System.out.println(url);
              product.setSoldQuantity(0);
              product.setRegistrationDate(new Date());
              product.setImage(url);
              productService.saveProduct(product);

              redirectAttributes.addFlashAttribute("messageSuccess", "The product has been saved successfully.");
              return "redirect:/admin/product/page/1";

        }


    }
    @GetMapping("/admin/product/edit/{id}")
    public String editProduct(@PathVariable("id") Integer id,RedirectAttributes redirectAttributes, Model model) {
        try {
            List<Brand> listBrands = brandService.getAllBrand();
            List<Category> listCategories = categoryService.findAllCategory();
            model.addAttribute("listBrands", listBrands);
            model.addAttribute("listCategories", listCategories);
            if (!model.containsAttribute("product")) {
                Product product = productService.getProductById(id);
                model.addAttribute("product", product);
            }
            return "product/new_product";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/admin/product/page/1";

        }
    }

    @PostMapping("/admin/product/edit/{id}")
    public String saveEditProduct(Product product, BindingResult errors, RedirectAttributes redirectAttributes,
                                  @PathVariable("id") Integer id, @RequestParam("file") MultipartFile file) {

        try {
            Product existProduct = productService.getProductById(id);
            Product productCheckUnique = productService.getProductByName(product.getName());

            if ( productCheckUnique != null && !productCheckUnique.getId().equals(existProduct.getId())) {
                errors.rejectValue("name", "product", "Tên sản phẩm không được trùng!");
            }
            if(product.getName().trim().length() == 0) {
                errors.rejectValue("name", "product", "Vui lòng nhập tên sản phẩm !");
            }
            if(product.getPrice() == null) {
                errors.rejectValue("price", "product", "Vui lòng nhập đơn giá !");
            }
            else if(product.getPrice() <= 0) {
                errors.rejectValue("price", "product", "Đơn giá phải lớn hơn 0 !");
            }
            if(product.getShortDescription().trim().length() == 0) {
                errors.rejectValue("shortDescription", "product", "Short description không được bỏ trống!");
            }
            if(product.getDescription().trim().length() == 0) {
                errors.rejectValue("description", "product", "Description không được bỏ trống!");
            }
            if(product.getDiscount() == null) {
                product.setDiscount(0);
            }
            if(product.getDiscount()  < 0 || product.getDiscount() > 100 ) {
                errors.rejectValue("discount", "product", "Discount  phải lon hon hoac bang 0 va be hon hoac bang 100!");
            }
            if(product.getInStock() == null) {
                errors.rejectValue("inStock", "product", "In stock không được bỏ trống!");
            }
            else if(product.getInStock() < 0) {
                errors.rejectValue("inStock", "product", "Số lượng phải nhiều hơn hoac bang 0 !");
            }

            if(product.getImage() == null) {
                errors.rejectValue("image", "product", "Image không được bỏ trống!");
            }
            if (errors.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.product", errors);
                redirectAttributes.addFlashAttribute("product", product);
                return "redirect:/admin/product/edit/" + id;
            } else {
                if(!existProduct.getImage().equals(product.getImage())) {
                    String url = storageService.upload(file);
                    product.setImage(url);
                }

                product.setSoldQuantity(existProduct.getSoldQuantity());
                product.setRegistrationDate(existProduct.getRegistrationDate());

                log.info(product.toString());
                productService.saveProduct(product);

                redirectAttributes.addFlashAttribute("messageSuccess", "The product has been edited successfully.");
                return "redirect:/admin/product/page/1";

            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/admin/product/page/1";
        }
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("messageSuccess", "The product ID " + id + " has been deleted successfully");
        }
        catch (Exception ex) {
            redirectAttributes.addFlashAttribute("messageError", ex.getMessage());
        }
        return "redirect:/admin/product/page/1";
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
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "product/detail_product";

        }
    }
}
