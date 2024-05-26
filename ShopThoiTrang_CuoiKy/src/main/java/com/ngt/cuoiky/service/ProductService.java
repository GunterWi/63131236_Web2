package com.ngt.cuoiky.service;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Product;
import com.ngt.cuoiky.repository.ProductRepository;

@Service
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);
    public static final int PRODUCT_PER_PAGE = 9;

    @Autowired
    private ProductRepository productRepository;

    public Product getProductByName(String name) {
        Product product = productRepository.getProductByName(name);

        return product;
    }

    public Product getProductById(Integer id){
        try {
            Product product = productRepository.findById(id).get();
            return product;

        } catch (NoSuchElementException ex) {
            System.err.println("Could not find any product with ID " + id);
            throw ex;
        }
    }
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        Long count = productRepository.countById(id);
        if (count == null || count == 0) {
            System.err.println("Could not find any product with ID " + id);
            throw new NoSuchElementException("Could not find any product with ID " + id);
        }

        productRepository.deleteById(id);
    }

    public Page<Product> listLatestProduct() {
        Pageable pageable = PageRequest.of(0, 10);
        return productRepository.findLatestProduct(pageable);
    }

    public Page<Product> listBestSellProduct() {
        Pageable pageable = PageRequest.of(0, 10);
        return productRepository.findBestSellProduct(pageable);
    }
    // admin
    public Page<Product> listByPage(Integer pageNum, String keyword) {
        Pageable pageable = null;

        pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE);

        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findAll(keyword, pageable);
        }
        return productRepository.findAll(pageable);
    }
}
