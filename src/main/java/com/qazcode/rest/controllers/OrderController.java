package com.qazcode.rest.controllers;

import com.qazcode.rest.entities.Order;
import com.qazcode.rest.services.OrderServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceInterface orderService;

    @GetMapping
    public List<Order> getAll(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id){
        return orderService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@Valid @RequestBody Order order){
        return orderService.createOrder(order.getCustomerId(),order.getAmount());
    }

    @PostMapping("/{id}/pay")
    public Order payOrder(@PathVariable Long id){
        return orderService.payOrder(id);
    }

    @PostMapping("/{id}/cancel")
    public Order cancelOrder(@PathVariable Long id){
        return orderService.cancelOrder(id);
    }

    @PutMapping("/{id}")
    public Order updateById(@PathVariable Long id, @RequestBody Order order){
        return orderService.updateOrder(id, order.getAmount());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        orderService.removeOrder(id);
    }
}
