package com.nqt.tutorial.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nqt.tutorial.models.Product;
import java.util.List;



public interface ProductRepository extends JpaRepository<Product,Long>{
    List<Product> findByProductName(String productName);
}
