package com.ngt.cuoiky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ngt.cuoiky.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select count(*) from (Select id, status_id From orders where user_id = :userId) AS ref inner join order_detail AS od\n" +
            "on ref.id = od.order_id and od.product_id = :productId and ref.status_id = 4", nativeQuery = true)
    public long countOrderByProductAndUser(Integer userId, Integer productId);
    
}