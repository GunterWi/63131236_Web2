package com.ngt.cuoiky.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ngt.cuoiky.model.Address;
import com.ngt.cuoiky.model.Cart;
import com.ngt.cuoiky.model.Order;
import com.ngt.cuoiky.model.OrderDetail;
import com.ngt.cuoiky.model.Product;
import com.ngt.cuoiky.model.User;
import com.ngt.cuoiky.repository.OrderRepository;
import com.ngt.cuoiky.repository.OrderStatusRepository;

@Service
public class OrderService {
    public static final int ORDERS_PER_PAGE = 5;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public Order getOrder(Integer id, User user) throws Exception {
        try {
            return orderRepository.findByIdAndUser(id, user.getId());


        }
        catch(NoSuchElementException ex) {
            throw new Exception("Could not find any order with ID " + id);

        }
    }

    public Order createOrder(User user, Address address, List<Cart> cartList) {
        Order newOrder = new Order();
        newOrder.setDate(new java.util.Date());
        newOrder.setAddress(address);
        newOrder.setUser(user);

        Set<OrderDetail> orderDetailSet = newOrder.getOrderDetails();
        double totalWithoutDiscount = 0.0;
        for (Cart cart : cartList) {
            Product product = cart.getProducts();
            product.setSoldQuantity(product.getSoldQuantity() + cart.getQuantity());
            product.setInStock(product.getInStock() - cart.getQuantity());
            OrderDetail orderDetail = new OrderDetail();


            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setUnitPrice(product.getDiscountPrice()); //price with discount
            orderDetail.setItemPrice(product.getPrice());
            orderDetail.setSubTotal(cart.getSubtotal());
            totalWithoutDiscount += cart.getSubtotal();

            orderDetailSet.add(orderDetail);
        }


        newOrder.setStatus(orderStatusRepository.getOrderStatusById(1));
        newOrder.setTotalPrice(totalWithoutDiscount);

        return orderRepository.save(newOrder);
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
