package com.ngt.cuoiky.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Brand;
import com.ngt.cuoiky.repository.BrandRepository;

@Service
public class BrandService {

    public static final int BRAND_PER_PAGE = 9;

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
    }

    public Page<Brand> listByPage(Integer pageNum, String keyword) {
        Pageable pageable = null;

        pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE);

        if (keyword != null && !keyword.isEmpty()) {
            return brandRepository.findAll(keyword, pageable);
        }
        return brandRepository.findAll(pageable);
    }
    
    public Brand getBrandByName(String name) {
        Brand brand = brandRepository.getBrandByName(name);
        return brand;
    }

    public Brand getBrandById(Integer id) {
        try {
            Brand brand = brandRepository.findById(id).get();
            return brand;

        } catch (NoSuchElementException ex) {
            System.err.println("Could not find any brand with ID " + id);
            throw ex;
        }
    }

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public void deleteBrand(Integer id) {
        Long count = brandRepository.countById(id);
        if (count == null || count == 0) {
            System.err.println("Could not find any brand with ID " + id);
            throw new NoSuchElementException("Could not find any brand with ID " + id);
        }

        brandRepository.deleteById(id);
    }
}
