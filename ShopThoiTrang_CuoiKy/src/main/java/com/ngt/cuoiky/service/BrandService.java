package com.ngt.cuoiky.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Brand;
import com.ngt.cuoiky.repository.BrandRepository;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> getAllBrand() {
        return brandRepository.findAll();
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
}
