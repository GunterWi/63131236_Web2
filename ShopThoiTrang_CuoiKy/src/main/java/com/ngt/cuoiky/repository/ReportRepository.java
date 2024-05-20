package com.ngt.cuoiky.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ngt.cuoiky.model.Order;

@Repository
public interface ReportRepository extends JpaRepository<Order, Integer> {
    @Query(value = "SELECT count(*)\n" +
            "FROM   orders\n" +
            "WHERE  YEARWEEK(`date`, 1) = YEARWEEK(CURDATE(), 1) and status_id = 1", nativeQuery = true)
    public long countOrderByWeek();

    @Query(value = "SELECT count(*)\n" +
            "FROM   users\n" +
            "WHERE  YEARWEEK(`created_at`, 1) = YEARWEEK(CURDATE(), 1)", nativeQuery = true)
    public long countUserByWeek();

    @Query(value = "SELECT COALESCE(sum(total_price),0) FROM orders WHERE  YEARWEEK(`date`, 1) = YEARWEEK(CURDATE(), 1) AND status_id = 4", nativeQuery = true)
    public long totalOrderByWeek();
}
