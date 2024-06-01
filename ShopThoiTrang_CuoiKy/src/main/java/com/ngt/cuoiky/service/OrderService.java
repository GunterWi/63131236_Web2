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
import com.ngt.cuoiky.repository.ProductRepository;

@Service
public class OrderService {
    public static final int ORDERS_PER_PAGE = 5;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order getOrder(Integer id, User user) throws Exception {
        try {
            return orderRepository.findByIdAndUser(id, user.getId());


        }
        catch(NoSuchElementException ex) {
            throw new Exception("Could not find any order with ID " + id);

        }
    }

    public void createOrder(User user, Address address, List<Cart> cartList) {
        Order newOrder = new Order();
        newOrder.setDate(new java.util.Date()); // Date nơw
        newOrder.setAddress(address);
        newOrder.setUser(user);

        Set<OrderDetail> orderDetailSet = newOrder.getOrderDetails();
        double totalWithoutDiscount = 0.0;
        for (Cart cart : cartList) {
            Product product = cart.getProducts();
            product.setSoldQuantity(product.getSoldQuantity() + cart.getQuantity()); // update sold quantity
            product.setInStock(product.getInStock() - cart.getQuantity()); // update in stock
            OrderDetail orderDetail = new OrderDetail();


            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cart.getQuantity()); 
            orderDetail.setUnitPrice(product.getDiscountPrice()); //price with discount
            orderDetail.setItemPrice(product.getPrice()); // price without discount
            orderDetail.setSubTotal(cart.getSubtotal());  // price with discount * quantity
            totalWithoutDiscount += cart.getSubtotal(); 
            orderDetailSet.add(orderDetail);
        }


        newOrder.setStatus(orderStatusRepository.getOrderStatusById(1)); // set status to "waiting for confirmation"
        newOrder.setTotalPrice(totalWithoutDiscount); // total price without discount
        orderRepository.save(newOrder);
    }

    public boolean isUserHasBuyProduct(Integer userId, Integer productId) {
        long num = orderRepository.countOrderByProductAndUser(userId, productId);
        System.out.println("userId: " + userId + ", productId: " + productId + ", num: " + num);
        return num > 0;
    }
    public Page<Order> listByPage(int pageNum, String keyword, Integer status) {
        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE);
        // search by keyword and status
        if (keyword != null) {
            return orderRepository.findByKeyword(keyword, status, pageable);
        }
        // search by status
        return orderRepository.findAll(status, pageable);
    }
    
    public Page<Order> listForUserByPage(User user, int pageNum, String keyword, Integer status) {
        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE);

        if (keyword != null) {
            return orderRepository.findByUserKeyword(keyword, user.getId(), status, pageable);
        }

        return orderRepository.findUserAll(user.getId(), status, pageable);

    }

    public void acceptOrder(Integer id, Integer statusId) throws Exception {
        try {
            Order order = orderRepository.findByOrderId(id);

            switch (statusId) {
                case 1: { //Neu dang cho xac nhan thi -> cho giao hang
                    order.setStatus(orderStatusRepository.getOrderStatusById(6));
                    break;
                }
                case 2: { //Neu dang cho yeu cau huy thi -> Da huy
                    List<OrderDetail> orderDetail = orderRepository.getOrderDetail(id);

                    for(OrderDetail item : orderDetail)
                    {
                        Product product = item.getProduct();
                        product.setSoldQuantity(product.getSoldQuantity() - item.getQuantity());
                        product.setInStock(product.getSoldQuantity() + item.getQuantity());
                        productRepository.save(product);
                    }
                    order.setStatus(orderStatusRepository.getOrderStatusById(5));
                    break;
                }
                case 3: { //Neu dang giao thi -> da giao
                    order.setStatus(orderStatusRepository.getOrderStatusById(4));
                    break;
                }
                case 6: { //Neu dang cho giao hang -> dang giao hang
                    order.setStatus(orderStatusRepository.getOrderStatusById(3));
                    break;
                }
                default:
                    break;
            }
            orderRepository.save(order);
        }
        catch(NoSuchElementException ex) {
            throw new Exception("Could not find any order with ID " + id);

        }
    }

    public void denyOrder(Integer id, Integer statusId) throws Exception {
        try {
            Order order = orderRepository.findByOrderId(id);

            switch (statusId) {
                case 1:
                case 6:
                case 3: { //Neu dang cho xac nhan, dang giao, dang cho giao thi -> huy
                    List<OrderDetail> orderDetail = orderRepository.getOrderDetail(id);

                    for(OrderDetail item : orderDetail)
                    {
                        Product product = item.getProduct();
                        product.setInStock(product.getInStock() + item.getQuantity());
                        product.setSoldQuantity(product.getSoldQuantity() - item.getQuantity());
                        productRepository.save(product);
                    }

                    order.setStatus(orderStatusRepository.getOrderStatusById(5));

                    break;
                }
                case 2: { //Neu dang yeu cau huy thi -> cho xac nhan
                    order.setStatus(orderStatusRepository.getOrderStatusById(1));
                    break;
                }
                default:
                    break;
            }
            orderRepository.save(order);
        }
        catch(NoSuchElementException ex) {
            throw new Exception("Could not find any order with ID " + id);

        }
    }

    public void requestCancel(Integer id) throws Exception {
        try {
            Order order = orderRepository.findByOrderId(id);
            order.setStatus(orderStatusRepository.getOrderStatusById(2));
            orderRepository.save(order);
        }
        catch(NoSuchElementException ex) {
            throw new Exception("Could not find any order with ID " + id);

        }
    }
    public void cancelRequest(Integer id) throws Exception {
        try {
            Order order = orderRepository.findByOrderId(id);

            order.setStatus(orderStatusRepository.getOrderStatusById(1));

            orderRepository.save(order);
        }
        catch(NoSuchElementException ex) {
            throw new Exception("Could not find any order with ID " + id);

        }
    }
}
