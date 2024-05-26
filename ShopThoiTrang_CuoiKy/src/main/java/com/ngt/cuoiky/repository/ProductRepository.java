package com.ngt.cuoiky.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ngt.cuoiky.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("Select p from Product p WHERE p.name = :name")
    public Product getProductByName(String name);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% "
            + "OR p.shortDescription LIKE %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);

    @Query("SELECT COUNT(p.id) from Product p WHERE p.id = :id")
    public Long countById(Integer id);

    @Query("SELECT p FROM Product  p WHERE p.isActive <> FALSE ORDER BY p.registrationDate DESC")
    public Page<Product> findLatestProduct(Pageable pageable);

    @Query("SELECT p FROM Product  p WHERE p.isActive <> FALSE ORDER BY p.soldQuantity DESC")
    public Page<Product> findBestSellProduct(Pageable pageable);
}
