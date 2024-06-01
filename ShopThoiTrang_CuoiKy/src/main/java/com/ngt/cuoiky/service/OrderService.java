package com.ngt.cuoiky.service;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Order;
import com.ngt.cuoiky.model.User;
import com.ngt.cuoiky.repository.OrderRepository;

@Service
public class OrderService {
    public static final int ORDERS_PER_PAGE = 5;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
     public Order getOrder(Integer id, User user) throws Exception {
        try {
            return orderRepository.findByIdAndUser(id, user.getId());


        }
        catch(NoSuchElementException ex) {
            throw new Exception("Could not find any order with ID " + id);

        }
    }
    
    public boolean isUserHasBuyProduct(Integer userId, Integer productId) {
        long num = orderRepository.countOrderByProductAndUser(userId, productId);
        System.out.println("userId: " + userId + ", productId: " + productId + ", num: " + num);
        return num > 0;
    }
    public Page<Order> listByPage(int pageNum, String keyword, String status) {
        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE);
        // search by keyword and status
        if (keyword != null) {
            return orderRepository.findByKeyword(keyword, status, pageable);
        }
        // search by status
        return orderRepository.findAll(status, pageable);
    }
    
    public Page<Order> listForUserByPage(User user, int pageNum, String keyword, String status) {
        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE);

        if (keyword != null) {
            return orderRepository.findByUserKeyword(keyword, user.getId(), status, pageable);
        }

        return orderRepository.findUserAll(user.getId(), status, pageable);

    }
}
