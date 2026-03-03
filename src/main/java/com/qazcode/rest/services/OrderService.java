package com.qazcode.rest.services;

import com.qazcode.rest.entities.Order;
import com.qazcode.rest.entities.OrderStatus;
import com.qazcode.rest.repositories.CustomerRepository;
import com.qazcode.rest.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface{

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(Long customerId, BigDecimal amount) {

        if(!customerRepository.existsById(customerId)){
            throw new EntityNotFoundException("No such client id: "+customerId);
        }

        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Amount should be positive: "+amount);
        }

        Order createdOrder = new Order();

        createdOrder.setCustomerId(customerId);
        createdOrder.setAmount(amount);
        createdOrder.setOrderStatus(OrderStatus.NEW);

        return orderRepository.save(createdOrder);
    }

    @Override
    public List<Order> getAllOrders(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy));

        List<Order> orders = orderRepository.findAll(pageable).getContent();
        if(orders.isEmpty()){
            throw new EntityNotFoundException("No orders right now: ");
        }
        return orders;
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No such order: "+id ));
    }

    @Transactional
    @Override
    public Order updateOrder(Long id, BigDecimal newAmount) {
        Order order = getById(id);
        if(order.getOrderStatus()!=OrderStatus.NEW){
            throw new IllegalArgumentException("Cannot change amount of paid/cancelled order");
        }
        if(newAmount == null){
            throw new IllegalArgumentException("amount can't be null");
        }
        if(newAmount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("amount must be positive");
        }
        order.setAmount(newAmount);

        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order payOrder(Long id){
        Order order = getById(id);
        if(order.getOrderStatus() != OrderStatus.NEW){
            throw new IllegalArgumentException("Only new can be paid");
        }
        order.setOrderStatus(OrderStatus.PAID);
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public Order cancelOrder(Long id){
        Order order = getById(id);
        if(order.getOrderStatus() == OrderStatus.PAID){
            throw new IllegalArgumentException("Paid orders can't be cancelled");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public void removeOrder(Long id) {if (!orderRepository.existsById(id)) {
        throw new EntityNotFoundException("No such order id: " + id);
    }
        orderRepository.deleteById(id);
    }
}
