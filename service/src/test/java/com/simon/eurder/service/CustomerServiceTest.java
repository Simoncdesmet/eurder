package com.simon.eurder.service;

import com.simon.eurder.domain.Customer;
import com.simon.eurder.domain.CustomerAddress;
import com.simon.eurder.domain.CustomerRepository;
import com.simon.eurder.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerServiceTest {

    CustomerRepository customerRepository;
    CustomerService customerService;
    Customer customer;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepository();
        customerService = new CustomerService(customerRepository);
        CustomerAddress customerAddress = new CustomerAddress(
                "Leeuwerikenstraat",
                "101/3",
                "3001",
                "Heverlee");
        customer = new Customer(
                "Test001",
                "Simon",
                "Desmet",
                "simoncdesmetgmail.com",
                "0487/57.70.40",
                customerAddress );
    }

    @Test
    void whenCreatingCustomerThroughService_customerIsInRepository() {

        customerService.createCustomer(customer);

        Assertions.assertTrue(customerRepository.getCustomers().contains(customer));

    }
}