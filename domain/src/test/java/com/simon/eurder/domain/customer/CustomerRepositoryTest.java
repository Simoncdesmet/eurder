package com.simon.eurder.domain.customer;


import com.simon.eurder.domain.DomainTestApp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = DomainTestApp.class)
public class CustomerRepositoryTest {

    @Value("${server.port}")
    private int port;


    private final CustomerDBRepository customerDBRepository;
    private Customer customer;

    @Autowired
    public CustomerRepositoryTest(CustomerDBRepository customerDBRepository) {
        this.customerDBRepository = customerDBRepository;
    }

    @BeforeEach
    void setUp() {
//        customerDBRepository.clearCustomers();

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

    @AfterEach
    void tearDown() {
        customerDBRepository.clearCustomers();
    }

    @Test
    void whenAddingCustomer_CustomerIsInDB() {
        customerDBRepository.createCustomer(customer);
        Assertions.assertEquals(1,
                customerDBRepository.getCustomers().stream()
                        .filter(c -> c.getCustomerID()
                                .equals("Test001")).count());

    }

    @Test
    void whenGettingAllCustomers_SizeIsOne() {
        customerDBRepository.createCustomer(customer);
        Assertions.assertEquals(1,
                customerDBRepository.getCustomers().size());
    }

    @Test
    void whenGettingCustomerTest001_returnsCustomer() {
        customerDBRepository.createCustomer(customer);
        Assertions.assertEquals("simoncdesmet@gmail.com",
                customerDBRepository.getCustomerByID("Test001").getEmail());
    }
}
