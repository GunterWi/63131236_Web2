package thigk.ntu63131236.nguyenquocthai_ktgiuaki.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import thigk.ntu63131236.nguyenquocthai_ktgiuaki.models.Product;
import thigk.ntu63131236.nguyenquocthai_ktgiuaki.services.ProductServiceImpl;

@Controller
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/products")
    public String listProduct(Model model,
                          @RequestParam("size") Optional<Integer> size,
                          @RequestParam("page") Optional<Integer> page,
                          @RequestParam(value = "search", required = false) String search) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Product> productPage;

        if (search != null && !search.isEmpty()) {
            productPage = productService.searchProducts(search, PageRequest.of(currentPage - 1, pageSize));
        } else {
            productPage = productService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        }

        model.addAttribute("listProducts", productPage);
    
        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "product_list";
    }


    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add_product";
    }

    @PostMapping("/products/add")
    public String addProduct(@ModelAttribute Product product) {
        productService.addProduct(product);
        return "redirect:/products";
    }
}
