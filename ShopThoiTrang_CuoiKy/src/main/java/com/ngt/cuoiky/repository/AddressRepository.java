package com.ngt.cuoiky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ngt.cuoiky.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query("SELECT a FROM Address a WHERE a.user.id = :id")
    public List<Address> getAddressByUserId(Integer id);

    @Query("SELECT count(a) FROM Address a WHERE a.user.id = :id")
    long countAddressByUserId(Integer id);

    @Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.user.id = ?2")
    public Address findByIdAndUserId(Integer addressId, Integer userId);
    
    @Query("DELETE FROM Address a WHERE a.id = ?1 AND a.user.id = ?2")
    @Modifying
    public void deleteByIdAndUserId(Integer addressId, Integer userId);
}
