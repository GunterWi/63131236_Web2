package com.ngt.cuoiky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ngt.cuoiky.model.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    @Query("SELECT p FROM Province p ORDER BY p.fullName")
    List<Province> findAll();
}