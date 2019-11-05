package com.simon.eurder.domain.customer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public Customer getCustomerByID(String customerID) {
        try {
            return customers.stream()
                    .filter(customer -> customer.getCustomerID().equals(customerID))
                    .collect(Collectors.toList()).get(0);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Customer not found!");
        }
    }

    public void clearCustomers() {
        customers = new ArrayList<>();
    }
}
