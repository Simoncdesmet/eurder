package com.simon.eurder.service.customer;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createCustomer(Customer customer) {
        customerRepository.createCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.getCustomers();
    }
}
