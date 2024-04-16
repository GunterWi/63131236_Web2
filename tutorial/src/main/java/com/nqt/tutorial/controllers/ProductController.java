package com.nqt.tutorial.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nqt.tutorial.models.Product;
import com.nqt.tutorial.models.ResponseObject;
import com.nqt.tutorial.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping(path = "/api/v1/Product")
public class ProductController {
    @Autowired
    private ProductRepository repository;
    
    //http://localhost:8080/api/v1/Product
    @GetMapping("")
    List<Product> getAllProduct(){
        return repository.findAll();
    }
    @SuppressWarnings("null")
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findByID(@PathVariable Long id) {
        Optional<Product> foundProduct = repository.findById(id);
        if(foundProduct.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query product successfully", foundProduct) 
                );
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("false", "Cannot find product with id = "+id,"" ) 
                );
        }
    }
    // insert new Product with Post method
    // Postman: Raw, Json
    @SuppressWarnings("null")
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct){
        // products must not have the same name
        List<Product> founProducts = repository.findByProductName(newProduct.getProductName().trim());
        if(founProducts.size()>0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("faild", "Product name already taken","")
                );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert Product successfully", repository.save(newProduct))
                );
    }
    // update, upsert = update if found, otherwise insert
    @SuppressWarnings("null")
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@PathVariable Long id, @RequestBody Product newProduct) {        
        Product updateProduct= repository.findById(id)
                .map(product->{
                    product.setProductName(newProduct.getProductName());
                    product.setProductYear(newProduct.getProductYear());
                    product.setPrice(newProduct.getPrice());
                    return repository.save(product);
                }).orElseGet(()->{
                    newProduct.setId(id);
                    return repository.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Update Product sucessfully", updateProduct)
        );  
    }
    // Delect a product => DELETE method
    @SuppressWarnings("null")
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
        boolean exists = repository.existsById(id);
        if(exists){
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete product successfully", "")
            );   
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ResponseObject("ok", "Cannot find product to delete", "")
        );  
    }   
}
