package com.qazcode.rest.services;

import com.qazcode.rest.entities.Customer;

import java.util.List;

public interface CustomerServiceInterface {

    Customer createCustomer(String name, String email);
    List<Customer> getAllCustomers(int page, int size, String sortBy);
    Customer getById(Long id);
    Customer updateCustomer(Long id, Customer customer);
    void deleteCustomer(Long id);
}
