package com.simon.eurder.service.customer;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerAddress;
import com.simon.eurder.repository.CustomerRepository;
import com.simon.eurder.service.ServiceTestApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = ServiceTestApp.class)
class CustomerServiceTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
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
                customerAddress);
    }


    @Sql(scripts = {"classpath:delete-rows.sql"})
    @Test
    void whenCreatingCustomerThroughService_customerIsInRepository() {

        customerService.createCustomer(customer);
        Assertions.assertEquals(1, (int) customerRepository
                .findAll().stream()
                .filter(cus -> cus.getCustomerID()
                        .equals("Test001")).count());
    }
}