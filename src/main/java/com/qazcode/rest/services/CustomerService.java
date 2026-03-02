package com.qazcode.rest.services;

import com.qazcode.rest.entities.Customer;
import com.qazcode.rest.repositories.CustomerRepository;
import com.qazcode.rest.repositories.OrderRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerService implements CustomerServiceInterface{

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    @Override
    public Customer createCustomer(String name, String email) {

        if(customerRepository.findByEmail(email)!=null){
            throw new EntityExistsException("Email should be unique: "+email);
        }

        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setName(name);

        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        if(customers.isEmpty()){
            throw new EntityNotFoundException("no customers right now");
        }
        return customers;
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No such customer: "+ id));
    }

    @Override
    @Transactional
    public Customer updateCustomer(Long id, Customer newCustomer) {
        Customer customer = getById(id);
        customer.setName(newCustomer.getName());

        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new EntityNotFoundException("no such customer id: " + id);
        }
        if(orderRepository.existsByCustomerId(id)){
            throw new IllegalArgumentException("can't delete since customer have orders, need to delete them beforehand");
        }
        customerRepository.deleteById(id);
    }
}
