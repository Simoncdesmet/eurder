package com.simon.eurder.service.customer;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void createCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByID(String customerID) {
            return customerRepository.findByCustomerID(customerID).orElseThrow(
                    ()->new IllegalArgumentException("Customer not found!"));
    }

    public void clearCustomers() {
        customerRepository.deleteAll();
    }
}
