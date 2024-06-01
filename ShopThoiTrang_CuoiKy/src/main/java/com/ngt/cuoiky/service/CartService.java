package com.ngt.cuoiky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ngt.cuoiky.model.Cart;
import com.ngt.cuoiky.model.Product;
import com.ngt.cuoiky.model.User;
import com.ngt.cuoiky.repository.CartRepository;

@Service
@Transactional
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public List<Cart> findCartByUser(Integer id) {
        return cartRepository.findByUserId(id);
    }
    public Integer addProductToCart(Product product, User user, Integer quantity) throws Exception{
        Integer updatedQuantity = quantity;
        Cart cartItem = cartRepository.findByUserIdAndProductId(user.getId(), product.getId());
        // if cartItem is not null, it means the product is already in the cart and we just need to update the quantity
        if(cartItem != null) {
            if(cartItem.getQuantity() >= product.getInStock()) {
                throw new Exception("");
            }
            updatedQuantity = cartItem.getQuantity() + quantity;
        }
        else {
            cartItem = new Cart();
            cartItem.setProducts(product);
            cartItem.setUser(user);
        }

        cartItem.setQuantity(updatedQuantity);

        cartRepository.save(cartItem);
        return updatedQuantity;
    }
    public double updatedQuantity(Product product, User user, Integer quantity) {
        cartRepository.updateQuantity(quantity, user.getId(), product.getId());

        return product.getPrice() * quantity;
    }

    public void deleteCartItem(Integer userId, Integer productId) {
        cartRepository.deleteByUserAndProduct(userId, productId);
    }
    
    public void deleteCartItemByUser(Integer userId) {
        cartRepository.deleteByUser(userId);
    }
}
