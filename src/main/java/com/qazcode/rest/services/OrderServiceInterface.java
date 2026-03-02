package com.qazcode.rest.services;

import com.qazcode.rest.entities.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderServiceInterface {
    Order createOrder(Long customerId, BigDecimal amount);
    List<Order> getAllOrders();
    Order getById(Long id);
    Order updateOrder(Long id, BigDecimal newAmount);
    Order payOrder(Long id);
    Order cancelOrder(Long id);
    void removeOrder(Long id);
}
