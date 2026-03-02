package com.qazcode.rest.controllers;

import com.qazcode.rest.entities.Customer;
import com.qazcode.rest.services.CustomerServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceInterface customerService;

    @GetMapping
    public List<Customer> getAll(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id){
        return customerService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@Valid @RequestBody Customer customer){
        return customerService.createCustomer(customer.getName(), customer.getEmail());
    }

    @PutMapping("/{id}")
    public Customer updateById(@PathVariable Long id, @Valid @RequestBody Customer customer){
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        customerService.deleteCustomer(id);
    }
}
