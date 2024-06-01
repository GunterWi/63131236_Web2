package com.ngt.cuoiky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ngt.cuoiky.model.Ward;

@Repository
public interface WardRepository extends JpaRepository<Ward, Integer> {

    @Query("SELECT w FROM Ward w WHERE w.district.code = :districtCode ORDER BY w.fullName")
    List<Ward> findWardByDistrictCode(Integer districtCode);
}
