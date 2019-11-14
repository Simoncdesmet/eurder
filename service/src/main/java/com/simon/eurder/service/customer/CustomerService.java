package com.simon.eurder.service.customer;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerService {

    private CustomerDBRepository customerRepository;

    @Autowired
    public CustomerService(CustomerDBRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createCustomer(Customer customer) {
        customerRepository.createCustomer(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.getCustomers();
    }

    public Customer getCustomerByID(String customerID) {
            return customerRepository.getCustomerByID(customerID);
    }

    public void clearCustomers() {
        customerRepository.clearCustomers();
    }
}
