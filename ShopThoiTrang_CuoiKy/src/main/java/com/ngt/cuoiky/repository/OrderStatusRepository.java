package com.ngt.cuoiky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ngt.cuoiky.model.OrderStatus;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    @Query("SELECT os FROM OrderStatus os where  os.id = :id")
    public OrderStatus getOrderStatusById(int id);
}

