package com.ngt.cuoiky.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

import com.ngt.cuoiky.exceptions.CategoryNotFoundException;
import com.ngt.cuoiky.model.Category;
import com.ngt.cuoiky.repository.CategoryRepository;

public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategoryByName(String name) {
        Category category = categoryRepository.getCategoryByName(name);
        return category;
    }

    public Category getCategoryById(Integer id) throws CategoryNotFoundException {
        try {
            Category category = categoryRepository.findById(id).get();
            return category;

        } catch (NoSuchElementException ex) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);

        }
    }

    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }
}
