package com.simon.eurder.service.shipment;

import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.service.item.ItemService;
import com.simon.eurder.service.order.OrderService;
import com.simon.eurder.service.order.OrderServiceTestSetUp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipmentServiceTest {

    OrderServiceTestSetUp setUp;
    private OrderService orderService;
    private ShipmentService shipmentService;

    @BeforeEach
    void setUp() {
        OrderServiceTestSetUp setUp = new OrderServiceTestSetUp();
        ItemService itemService = setUp.createItemServiceWithGolfBallInRepository();
        CustomerService customerService = setUp.createCustomerServiceWithSimonDesmetInRepository();
        orderService = setUp.setUpOrderService(itemService, customerService);
        orderService.createOrder(List.of(new ItemGroup("Golf ball", 10)), "Test001");
        orderService.createOrder(List.of(new ItemGroup("Golf ball", 15)), "Test001");
        orderService.createOrder(List.of(new ItemGroup("Golf ball", 25)), "Test001");
        orderService.createOrder(List.of(new ItemGroup("Golf ball", 20)), "Test001");
        shipmentService = new ShipmentService(orderService, customerService);
    }

    @Test
    void given4Orders_whenGettingShipmentStatementForTomorrow_containsThreeOrders() {
        Assertions.assertThat(shipmentService.getShippingOverviewForDay(
                LocalDate.now().plus(1, ChronoUnit.DAYS))).contains("ordered: 10");
        Assertions.assertThat(shipmentService.getShippingOverviewForDay(
                LocalDate.now().plus(1, ChronoUnit.DAYS))).contains("ordered: 15");
        Assertions.assertThat(shipmentService.getShippingOverviewForDay(
                LocalDate.now().plus(1, ChronoUnit.DAYS))).contains("ordered: 25");
    }

    @Test
    void given4Orders_whenGettingShipmentStatementForTomorrow_doesNotContainOutOfStockOrder() {
        assertFalse(shipmentService.getShippingOverviewForDay(
                LocalDate.now().plus(1, ChronoUnit.DAYS)).contains("ordered: 20"));
    }

    @Test
    void given4Orders_whenGettingShipmentStatementForNextWeek_ContainsOutOfStockOrder() {
        assertTrue(shipmentService.getShippingOverviewForDay(
                LocalDate.now().plus(7, ChronoUnit.DAYS)).contains("ordered: 20"));
    }


}