package com.ngt.cuoiky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ngt.cuoiky.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Query("Select p from Brand p WHERE p.name = :name")
    public Brand getBrandByName(String name);
}
