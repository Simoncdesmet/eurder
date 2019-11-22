package com.simon.eurder.domain.order;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerAddress;
import com.simon.eurder.repository.OrderCrudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private OrderCrudRepository orderRepository;

    @Sql(scripts = {"classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenCreatingOrder_orderIsInRepository() {

        Order order = new Order(
                List.of(new ItemGroup("Golf ball", 50)),
                "Test001");

        orderRepository.save(order);
        assertTrue(orderRepository.findAll()
                .stream().map(Order::getExternalId)
                .collect(Collectors.toList())
                .contains(order.getExternalId()));

    }
}