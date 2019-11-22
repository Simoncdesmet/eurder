package com.simon.eurder.service.shipment;

import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.service.order.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
@Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql"})
class ShipmentServiceTest {


    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setUp() {

        orderService.createOrder(List.of(new ItemGroup("Golf ball", 10)), "Test001");
        orderService.createOrder(List.of(new ItemGroup("Golf ball", 15)), "Test001");
        orderService.createOrder(List.of(new ItemGroup("Golf ball", 25)), "Test001");
        orderService.createOrder(List.of(new ItemGroup("Golf ball", 20)), "Test001");
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void given4Orders_whenGettingShipmentStatementForTomorrow_containsThreeOrders() {
        Assertions.assertThat(shipmentService.getShippingOverviewForDay(
                LocalDate.now().plus(1, ChronoUnit.DAYS))).contains("ordered: 10");
        Assertions.assertThat(shipmentService.getShippingOverviewForDay(
                LocalDate.now().plus(1, ChronoUnit.DAYS))).contains("ordered: 15");
        Assertions.assertThat(shipmentService.getShippingOverviewForDay(
                LocalDate.now().plus(1, ChronoUnit.DAYS))).contains("ordered: 25");
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void given4Orders_whenGettingShipmentStatementForTomorrow_doesNotContainOutOfStockOrder() {
        assertFalse(shipmentService.getShippingOverviewForDay(
                LocalDate.now().plus(1, ChronoUnit.DAYS)).contains("ordered: 20"));
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void given4Orders_whenGettingShipmentStatementForNextWeek_ContainsOutOfStockOrder() {
        assertTrue(shipmentService.getShippingOverviewForDay(
                LocalDate.now().plus(7, ChronoUnit.DAYS)).contains("ordered: 20"));
    }


}