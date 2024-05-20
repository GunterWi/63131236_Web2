package com.ngt.cuoiky.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.exceptions.CategoryNotFoundException;
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
     public Page<Category> listByPage(Integer pageNum, String keyword, String sortField, String sortDir) {
        Pageable pageable = null;

        if(sortField != null && !sortField.isEmpty()) {
            Sort sort = Sort.by(sortField);
            sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
            pageable = PageRequest.of(pageNum - 1, CATEGORY_PER_PAGE, sort);
        }
        else {
            pageable = PageRequest.of(pageNum - 1, CATEGORY_PER_PAGE);
        }

        if (keyword != null && !keyword.isEmpty()) {
            return categoryRepository.findAll(keyword, pageable);
        }
        return categoryRepository.findAll(pageable);
    }
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) throws CategoryNotFoundException {
        Long count = categoryRepository.countById(id);
        if (count == null || count == 0) {
            throw new CategoryNotFoundException("Could not find any category with ID " + id);
        }

        categoryRepository.deleteById(id);
    }
}
