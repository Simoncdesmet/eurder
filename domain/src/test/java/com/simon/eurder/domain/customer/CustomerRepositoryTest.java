package com.simon.eurder.domain.customer;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerAddress;
import com.simon.eurder.domain.customer.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomerRepositoryTest {

    CustomerRepository customerRepository;
    Customer customer;

    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepository();
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
    void beforeAddingCustomer_customerIsNotInRepository() {
        Assertions.assertFalse(customerRepository.getCustomers().contains(customer));
    }

    @Test
    void whenCreatingCustomer_customerIsInRepository() {
        CustomerRepository customerRepository = new CustomerRepository();
        customerRepository.createCustomer(customer);

        Assertions.assertTrue(customerRepository.getCustomers().contains(customer));

    }
}