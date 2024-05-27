package com.ngt.cuoiky.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Category;
import com.ngt.cuoiky.repository.CategoryRepository;

@Service
public class CategoryService {

    public static final int CATEGORY_PER_PAGE = 9;

    @Autowired
    private CategoryRepository categoryRepository;
    

    public Category getCategoryByName(String name) {
        Category category = categoryRepository.getCategoryByName(name);
        return category;
    }

    public Category getCategoryById(Integer id) {
        try {
            Category category = categoryRepository.findById(id).get();
            return category;

        } catch (NoSuchElementException ex) {
            System.err.println("Could not find any category with ID " + id);
            throw ex;
        }
    }

    public List<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

    public Page<Category> listByPage(Integer pageNum, String keyword) {
        Pageable pageable = null;

        pageable = PageRequest.of(pageNum - 1, CATEGORY_PER_PAGE);

        if (keyword != null && !keyword.isEmpty()) {
            return categoryRepository.findAll(keyword, pageable);
        }
        return categoryRepository.findAll(pageable);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        Long count = categoryRepository.countById(id);
        if (count == null || count == 0) {
            System.err.println("Could not find any category with ID " + id); 
            throw new NoSuchElementException("Could not find any category with ID " + id);
        }

        categoryRepository.deleteById(id);
    }
}
