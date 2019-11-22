package com.simon.eurder.service.order;

import com.simon.eurder.domain.item.Item;
import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import com.simon.eurder.repository.OrderRepository;
import com.simon.eurder.service.ServiceTestApp;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.service.item.ItemService;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService.clearOrders();
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenCreatingOrderThroughService_orderForCustomerIDIsInRepository() {
        Order order = prepareSingleLineOrder("Golf ball", 50);
        orderService.createOrder(order.getItemGroups(), order.getCustomerID());
        assertTrue(orderService.getAllOrders().stream()
                .map(Order::getCustomerID).collect(Collectors.toList())
                .contains(order.getCustomerID()));
    }


    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenCreatingOrderOf50GolfBalls_totalPriceOfOrderIs50() {
        Order order = prepareSingleLineOrder("Golf ball", 50);
        Order loggedOrder = orderService.createOrder(order.getItemGroups(), order.getCustomerID());
        assertEquals(50, loggedOrder.getTotalPrice());
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenCreatingOrderWithItemThatDoesntExist_exceptionIsThrown() {
        Order wrongOrder = prepareSingleLineOrder("Non-existing Golf ball", 50);
        Assertions.assertThatThrownBy(() -> orderService.createOrder(wrongOrder.getItemGroups(), wrongOrder.getCustomerID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("This item does not exist!");
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenCreatingOrderWithEnoughStockAvailable_ShippingDateIsNextDay() {
        Order order = prepareSingleLineOrder("Golf ball", 50);
        orderService.createOrder(order.getItemGroups(), order.getCustomerID());
        assertEquals(orderService.getAllOrders().get(0)
                .getItemGroups().get(0)
                .getShippingDate(), LocalDate.now().plus(1, ChronoUnit.DAYS));

    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenCreatingOrderWithNotEnoughStockAvailable_ShippingDateIsNextWeek() {
        Order newOrder = prepareSingleLineOrder("Golf ball", 60);

        orderService.createOrder(newOrder.getItemGroups(), newOrder.getCustomerID());
        assertEquals(orderService.getAllOrders().get(0)
                .getItemGroups().get(0)
                .getShippingDate(), LocalDate.now().plus(7, ChronoUnit.DAYS));
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenCreatingOrderWithNonExistingCustomer_thenExceptionIsThrown() {
        Order orderWithNotExistingCustomer = new Order(
                List.of(new ItemGroup("Golf ball", 50)),
                "FakeCustomerID");

        Assertions.assertThatThrownBy(() -> orderService.createOrder(
                orderWithNotExistingCustomer.getItemGroups(), orderWithNotExistingCustomer.getCustomerID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No customer found with this id.");
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenPlacingOrder_itemNameIsAddedToItemGroups() {
        Order order = prepareSingleLineOrder("Golf ball", 10);
        orderService.createOrder(order.getItemGroups(), order.getCustomerID());
        assertEquals(orderService.getAllOrders().get(0)
                .getItemGroups().get(0).getItemName(), "Golf ball");
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenGettingOrderReportForCustomer_printOutIsReturned() {
        Order loggedOrder = createLoggedOrder();
        String orderSummary = orderService.createOrderSummary(loggedOrder);

        assertTrue(orderSummary.contains("Total price for order is: 30.0"));
        assertTrue(orderSummary.contains("Order with id: " + loggedOrder.getOrderID()));
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenReordering_orderIsRecreated() {
        Order loggedOrder = createLoggedOrder();
        Order reorderedOrder = orderService.reorder(loggedOrder.getOrderID());
        assertEquals(reorderedOrder.getCustomerID(), loggedOrder.getCustomerID());
        assertEquals(reorderedOrder.getTotalPrice(), loggedOrder.getTotalPrice());
        assertNotEquals(reorderedOrder.getOrderID(), loggedOrder.getOrderID());
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void whenReorderingOrderWhereThirdItemGroupWillBeOutOfStock_OnlyThirdItemGroupHasShippingDateOfNextWeek() {
        Order loggedOrder = createLoggedOrder();
        Order reorderedOrder = orderService.reorder(loggedOrder.getOrderID());

        assertEquals(reorderedOrder.getItemGroups().get(0).getShippingDate(), LocalDate.now().plus(1, ChronoUnit.DAYS));
        assertEquals(reorderedOrder.getItemGroups().get(1).getShippingDate(), LocalDate.now().plus(1, ChronoUnit.DAYS));
        assertEquals(reorderedOrder.getItemGroups().get(2).getShippingDate(), LocalDate.now().plus(7, ChronoUnit.DAYS));
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void givenUpdatedItem_whenReorderingOrder_orderUsesNewItemInformation() {
        Order newOrder = prepareOrderOfThirtyGolfBallsInThreeLines();

        Order loggedOrder = orderService.createOrder(newOrder.getItemGroups(), newOrder.getCustomerID());
        updateGolfBallItem();
        Order reorderedOrder = orderService.reorder(loggedOrder.getOrderID());

        assertEquals(reorderedOrder.getTotalPrice(), 60);
        assertTrue(reorderedOrder.getItemGroups()
                .stream()
                .map(ItemGroup::getItemName)
                .collect(Collectors.toList()).contains("Updated golf ball"));
    }

    @Sql(scripts = {"classpath:delete-rows.sql", "classpath:create-customer.sql", "classpath:create-item.sql"})
    @Test
    void givenUpdatedItem_whenReorderingOrder_oldOrderStaysTheSame() {
        Order loggedOrder = createLoggedOrder();
        updateGolfBallItem();
        orderService.reorder(loggedOrder.getOrderID());
        assertEquals(loggedOrder.getTotalPrice(), 30);
        assertFalse(loggedOrder.getItemGroups()
                .stream()
                .map(ItemGroup::getItemName)
                .collect(Collectors.toList()).contains("Updated golf ball"));
    }

    private Order createLoggedOrder() {
        Order order = prepareOrderOfThirtyGolfBallsInThreeLines();
        return orderService.createOrder(order.getItemGroups(), order.getCustomerID());
    }

    private void updateGolfBallItem() {
        itemService.updateItem("Golf ball", new Item("Updated golf ball", "Updated golf ball", 2, 50));
    }

    private Order prepareOrderOfThirtyGolfBallsInThreeLines() {
        return new Order(
                List.of(
                        new ItemGroup("Golf ball", 10),
                        new ItemGroup("Golf ball", 10),
                        new ItemGroup("Golf ball", 10)),
                customerService.getAllCustomers().get(0).getCustomerID());
    }

    private Order prepareSingleLineOrder(String externalItemId, int amount) {
        return new Order(
                List.of(new ItemGroup(externalItemId, amount)),
                customerService.getAllCustomers().get(0).getCustomerID());
    }
}