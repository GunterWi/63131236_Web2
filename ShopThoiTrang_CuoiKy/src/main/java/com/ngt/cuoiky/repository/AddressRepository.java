package com.ngt.cuoiky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ngt.cuoiky.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query("SELECT a FROM Address a WHERE a.user.id = :id")
    public List<Address> getAddressByUserId(Integer id);
    
    @Query("SELECT count(a) FROM Address a WHERE a.user.id = :id")
    long countAddressByUserId(Integer id);
}
