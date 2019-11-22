package com.simon.eurder.domain.order;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.simon.eurder.repository.OrderRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    private Customer customer;
    private OrderRepository orderRepository;

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

        orderRepository = new OrderRepository();
    }

    @Test
    void whenCreatingOrder_orderIsInRepository() {

        Order order = new Order(
                List.of(new ItemGroup("Golf ball", 50)),
                customer.getCustomerID());

        orderRepository.createOrder(order);
        assertTrue(orderRepository.getAllOrders().contains(order));

    }
}