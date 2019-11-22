package com.simon.eurder.domain.customer;


import com.simon.eurder.domain.DomainTestApp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.simon.eurder.repository.CustomerRepository;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
public class CustomerRepositoryTest {

    @Value("${server.port}")
    private int port;

    @Autowired
    private CustomerRepository customerRepository;
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
                "simoncdesmet@gmail.com",
                "0487/57.70.40",
                customerAddress);
    }


    @Sql(scripts = {"classpath:delete-rows.sql"})
    @Test
    void whenAddingCustomer_CustomerIsInDB() {
        customerRepository.save(customer);
        Assertions.assertEquals(1,
                customerRepository.findByCustomerID(customer.getCustomerID()).stream()
                        .filter(c -> c.getCustomerID()
                                .equals("Test001")).count());

    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql"})
    @Test
    void whenGettingAllCustomers_SizeIsOne() {
        Assertions.assertEquals(1,
                customerRepository.findAll().size());
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql"})
    @Test
    void whenGettingCustomerTest001_returnsCustomer() {
        Assertions.assertEquals("simoncdesmet@gmail.com",
                customerRepository.findByCustomerID("Test001").get().getEmail());
    }
}
