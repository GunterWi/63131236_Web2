package com.ngt.cuoiky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Cart;
import com.ngt.cuoiky.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public List<Cart> findCartByUser(Integer id) {
        return cartRepository.findByUserId(id);
    }
}
