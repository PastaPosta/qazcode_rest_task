package com.qazcode.rest.servicesTests;

import com.qazcode.rest.entities.Order;
import com.qazcode.rest.entities.OrderStatus;
import com.qazcode.rest.repositories.CustomerRepository;
import com.qazcode.rest.repositories.OrderRepository;
import com.qazcode.rest.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_success(){
        Long customerId = 1L;
        BigDecimal amount = new BigDecimal("100.00");

        when(customerRepository.existsById(customerId)).thenReturn(true);

        when(orderRepository.save(any(Order.class))).thenAnswer(invoc -> invoc.<Order>getArgument(0));

        Order result = orderService.createOrder(customerId, amount);

        assertNotNull(result);
        assertEquals(result.getAmount(), amount);
        assertEquals(OrderStatus.NEW, result.getOrderStatus());
        assertEquals(customerId, result.getCustomerId());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_CustomerNotFound_Throws(){
        Long customerId = 200L;
        BigDecimal amount = new BigDecimal("100.00");

        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(customerId, amount));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void payNewOrder_Success(){
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setOrderStatus(OrderStatus.NEW);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order result = orderService.payOrder(orderId);

        assertEquals(OrderStatus.PAID, result.getOrderStatus());
        verify(orderRepository, times(1)).save(mockOrder);
    }
    @Test
    void payPaidOrder_Throws(){
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setOrderStatus(OrderStatus.PAID);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        assertThrows(IllegalArgumentException.class, () -> orderService.payOrder(orderId));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void payCancelledOrder_Throws(){
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setOrderStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        assertThrows(IllegalArgumentException.class, () -> orderService.payOrder(orderId));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void cancelPaidOrder_Throws(){
        Long orderId = 1L;
        Order mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setOrderStatus(OrderStatus.PAID);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        assertThrows(IllegalArgumentException.class, () ->
            orderService.cancelOrder(orderId)
        );

        verify(orderRepository, never()).save(any());
    }
}
