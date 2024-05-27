package com.ngt.cuoiky.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ngt.cuoiky.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Query("Select p from Brand p WHERE p.name = :name")
    public Brand getBrandByName(String name);

    @Query("SELECT COUNT(p.id) from Brand p WHERE p.id = :id")
    public Long countById(Integer id);

    @Query("SELECT p FROM Brand p WHERE p.name LIKE %:keyword% "
            + "OR p.description LIKE %:keyword% ")
    public Page<Brand> findAll(String keyword, Pageable pageable);
}
