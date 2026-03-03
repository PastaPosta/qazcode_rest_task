package com.qazcode.rest.servicesTests;


import com.qazcode.rest.entities.Customer;
import com.qazcode.rest.repositories.CustomerRepository;
import com.qazcode.rest.repositories.OrderRepository;
import com.qazcode.rest.services.CustomerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CustomerService customerService;

    //create(fail,success), update(fail, success), delete(fail,success)
    @Test
    void createCustomer_Success(){
        String name = "Alex";
        String email = "alex@gmail.com";

        when(customerRepository.findByEmail(email)).thenReturn(null);

        when(customerRepository.save(any(Customer.class))).thenAnswer(invoc -> invoc.<Customer>getArgument(0));

        Customer result = customerService.createCustomer(name, email);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createCustomer_Fail_Throws(){
        String name = "Alex";
        String email = "alex@gmail.com";

        when(customerRepository.findByEmail(email)).thenReturn(new Customer());

        assertThrows(EntityExistsException.class, () -> customerService.createCustomer(name, email));

        verify(customerRepository, never()).save(any());
    }

    @Test
    void updateCustomer_Success(){
        Long customerId = 1L;
        Customer mockCustomer = new Customer();
        String email = "alex@gmail.com";
        String oldName = "Alexander";
        mockCustomer.setName(oldName);
        mockCustomer.setEmail(email);

        Customer mockNewCustomer = new Customer();
        String newName = "Alexnew";
        mockNewCustomer.setName(newName);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invoc -> invoc.<Customer>getArgument(0));

        Customer result = customerService.updateCustomer(customerId, mockNewCustomer);

        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(email, result.getEmail());

        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void updateCustomer_Fail_Throws(){
        Long customerId = 1L;
        Customer mockCustomer = new Customer();
        String email = "alex@gmail.com";
        String oldName = "Alexander";
        mockCustomer.setName(oldName);
        mockCustomer.setEmail(email);

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.updateCustomer(customerId, mockCustomer));

        verify(customerRepository, never()).save(any());
    }
    @Test
    void deleteCustomer_Success(){
        Long customerId = 1L;

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(orderRepository.existsByCustomerId(customerId)).thenReturn(false);

        customerService.deleteCustomer(customerId);

        verify(customerRepository, times(1)).deleteById(customerId);
    }

    @Test
    void deleteCustomer_FailOrders_Throws() {
        Long customerId = 1L;

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(orderRepository.existsByCustomerId(customerId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> customerService.deleteCustomer(customerId));

        verify(customerRepository, never()).deleteById(any());
    }

    @Test
    void deleteCustomer_FailNotExists_Throws(){
        Long customerId = 1L;

        when(customerRepository.existsById(customerId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomer(customerId));

        verify(customerRepository, never()).deleteById(any());
    }
}
