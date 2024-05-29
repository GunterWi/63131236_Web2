package com.ngt.cuoiky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.OrderStatus;
import com.ngt.cuoiky.repository.OrderStatusRepository;

@Service
public class OrderStatusService {

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public List<OrderStatus> listOrderStatus() {
        return orderStatusRepository.findAll();
    }
}
