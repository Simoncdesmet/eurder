package com.simon.eurder.domain.customer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerRepository {


    private List<Customer> customers;

    public CustomerRepository() {
        this.customers = new ArrayList<>();
    }

    public List<Customer> getCustomers() {

    return customers;
    }

    public void createCustomer(Customer customer) {
        customers.add(customer);
    }
}
