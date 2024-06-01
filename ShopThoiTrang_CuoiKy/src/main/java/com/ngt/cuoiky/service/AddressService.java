package com.ngt.cuoiky.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Address;
import com.ngt.cuoiky.model.District;
import com.ngt.cuoiky.model.Province;
import com.ngt.cuoiky.model.Ward;
import com.ngt.cuoiky.repository.AddressRepository;
import com.ngt.cuoiky.repository.DistrictRepository;
import com.ngt.cuoiky.repository.ProvinceRepository;
import com.ngt.cuoiky.repository.WardRepository;

@Service
public class AddressService {
    
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WardRepository wardRepository;

    public List<Province> getListProvinces() {
        return provinceRepository.findAll();
    }

    public List<District> getListDistrict() {
        return districtRepository.findAll();
    }

    public List<Ward> getListWard() {
        return wardRepository.findAll();
    }

    public List<Address> getListAddressByUserId(Integer id) {
        return addressRepository.getAddressByUserId(id);
    }

    public long getCountAddressByUserId(Integer id) {
        return addressRepository.countAddressByUserId(id);
    }
    public void delete(Integer addressId, Integer userId) {
        addressRepository.deleteByIdAndUserId(addressId, userId);
    }
    public Address save(Address address) {
        return addressRepository.save(address);
    }
    public Address getAddress(Integer addressId, Integer userId) throws Exception {
        try {
            return addressRepository.findByIdAndUserId(addressId, userId);
        }
        catch(NoSuchElementException ex) {
            throw new Exception("Could not find any address with ID " + addressId);

        }
    }
}
