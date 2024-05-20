package com.ngt.cuoiky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ngt.cuoiky.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    @Query("SELECT c FROM Cart c WHERE c.user.id = :id")
    public List<Cart> findByUserId(Integer id);

    @Query("SELECT c FROM Cart c WHERE c.products.id = :productId AND c.user.id = :userId")
    public Cart findByUserIdAndProductId(Integer userId, Integer productId);

    @Modifying
    @Query("UPDATE Cart c SET c.quantity = :quantity WHERE c.products.id = :productId AND c.user.id = :userId")
    public void updateQuantity(Integer quantity, Integer userId, Integer productId);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.products.id = :productId AND c.user.id = :userId ")
    public void deleteByUserAndProduct(Integer userId, Integer productId);

    @Modifying
    @Query("DELETE FROM Cart  c WHERE  c.user.id = :userId")
    public void deleteByUser(Integer userId);
}
