package com.ngt.cuoiky.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ngt.cuoiky.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

   @Query("SELECT p FROM Category p WHERE p.name LIKE %:keyword% "
            + "OR p.description LIKE %:keyword% ")
    public Page<Category> findAll(String keyword, Pageable pageable);

    @Query("SELECT COUNT(p.id) from Category p WHERE p.id = :id")
    public Long countById(Integer id);

    @Query("Select c from Category c WHERE c.name = :name")
    public Category getCategoryByName(String name);
}
