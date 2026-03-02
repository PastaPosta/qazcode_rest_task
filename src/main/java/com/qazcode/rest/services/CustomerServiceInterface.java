package com.qazcode.rest.services;

import com.qazcode.rest.entities.Customer;

import java.util.List;

public interface CustomerServiceInterface {

    Customer createCustomer(String name, String email);
    List<Customer> getAllCustomers();
    Customer getById(Long id);
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
}
