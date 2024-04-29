package com.ngt.cuoiky.service;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.exceptions.ProductNotFoundException;
import com.ngt.cuoiky.model.Product;
import com.ngt.cuoiky.repository.ProductRepository;

@Service
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public Product getProductByName(String name) {
        Product product = productRepository.getProductByName(name);

        return product;
    }

    public Product getProductById(Integer id) throws ProductNotFoundException {
        try {
            Product product = productRepository.findById(id).get();
            return product;

        } catch (NoSuchElementException ex) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);

        }
    }

    public Page<Product> listLatestProduct() {
        Pageable pageable = PageRequest.of(0, 10);
        return productRepository.findLatestProduct(pageable);
    }

    public Page<Product> listBestSellProduct() {
        Pageable pageable = PageRequest.of(0, 10);
        return productRepository.findBestSellProduct(pageable);
    }
}
