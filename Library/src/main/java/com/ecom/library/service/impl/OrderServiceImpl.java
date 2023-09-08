package com.ecom.library.service.impl;


import com.ecom.library.model.*;
import com.ecom.library.repository.CustomerRepository;
import com.ecom.library.repository.OrderDetailRepository;
import com.ecom.library.repository.OrderRepository;
import com.ecom.library.service.OrderService;
import com.ecom.library.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/*  ----------------------------------Order Service Implementation----------------------------------------    */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository detailRepository;
    private final CustomerRepository customerRepository;
    private final ShoppingCartService cartService;

    /*  ----------------------------------save order----------------------------------------------------    */
    @Override
    public Order save(ShoppingCart shoppingCart) {
        //Create new order
       Order order = new Order();
       //Set customer, date, tax,quantity, price and payment option
       order.setOrderDate(new Date());
       order.setCustomer(shoppingCart.getCustomer());
       order.setTax(2);
       order.setTotalPrice(shoppingCart.getTotalPrice());
       order.setAccept(false);
       order.setPaymentMethod("Cash");
       order.setOrderStatus("Pending");
       order.setQuantity(shoppingCart.getTotalItems());

       //Save order details of every item
       List<OrderDetail> orderDetailList = new ArrayList<>();
       for (CartItem item : shoppingCart.getCartItems()) {
           OrderDetail orderDetail = new OrderDetail();
           orderDetail.setOrder(order);
           orderDetail.setProduct(item.getProduct());
           //save item-level details
           detailRepository.save(orderDetail);
           orderDetailList.add(orderDetail);
       }
       order.setOrderDetailList(orderDetailList);
       cartService.deleteCartById(shoppingCart.getId());
       return orderRepository.save(order);
    }

    /*  ----------------------------------Find all orders of username---------------------------------------------------    */
    @Override
    public List<Order> findAll(String username) {
        Customer customer = customerRepository.findByUsername(username);
        List<Order> orders = customer.getOrders();
        return orders;
    }

    /*  ----------------------------------Find all orders----------------------------------------------------    */
    @Override
    public List<Order> findALlOrders() {
        return orderRepository.findAll();
    }

    /*  ----------------------------------Accept order----------------------------------------------------    */
    @Override
    public Order acceptOrder(Long id) {
        Order order = orderRepository.getById(id);
        order.setAccept(true);
        order.setDeliveryDate(new Date());
        return orderRepository.save(order);
    }

    /*  ----------------------------------Cancel order----------------------------------------------------    */
    @Override
    public void cancelOrder(Long id) {
        //Delete order from repository
        orderRepository.deleteById(id);
    }
}
