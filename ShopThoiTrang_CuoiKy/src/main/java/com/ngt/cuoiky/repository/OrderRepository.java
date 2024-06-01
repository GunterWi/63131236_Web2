package com.ngt.cuoiky.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ngt.cuoiky.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.id = :id")
    public Order findByIdAndUser(Integer id, Integer userId);

    @Query(value = "select count(*) from (Select id, status_id From orders where user_id = :userId) AS ref inner join order_detail AS od\n" +
            "on ref.id = od.order_id and od.product_id = :productId and ref.status_id = 4", nativeQuery = true)
    public long countOrderByProductAndUser(Integer userId, Integer productId);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderDetails od JOIN od.product p "
            + "WHERE p.name LIKE %:keyword% OR o.user.firstName LIKE %:keyword% OR o.user.lastName LIKE %:keyword% AND "
            + "(( :status = 'ALL' AND o.status IS NOT NULL) OR (:status <> 'ALL' AND o.status.name = :status))")
    public Page<Order> findByKeyword(String keyword, String status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE " 
            + "((:status = 'ALL' AND o.status IS NOT NULL) OR (:status <> 'ALL' AND o.status.name = :status))")
    public Page<Order> findAll(String status, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderDetails od JOIN od.product p "
            + "WHERE o.user.id = :userId AND p.name LIKE %:keyword% AND " 
            + "(( :status = 'ALL' AND o.status IS NOT NULL) OR (:status <> 'ALL' AND o.status.name = :status))")
    public Page<Order> findByUserKeyword(String keyword, Integer userId, String status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND " 
            + "(( :status = 'ALL' AND o.status IS NOT NULL) OR (:status <> 'ALL' AND o.status.name = :status))")
    public Page<Order> findUserAll(Integer userId,String status, Pageable pageable);
}