package com.simon.eurder.service.order;

import com.simon.eurder.domain.item.Item;
import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.service.item.ItemService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private CustomerService customerService;
    private ItemService itemService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        OrderServiceTestSetUp setUp = new OrderServiceTestSetUp();
        itemService = setUp.createItemServiceWithGolfBallInRepository();
        customerService = setUp.createCustomerServiceWithSimonDesmetInRepository();
        orderService = setUp.setUpOrderService(itemService, customerService);
    }

    @Test
    void whenCreatingOrderThroughService_orderForCustomerIDIsInRepository() {
        Order order = prepareSingleLineOrder("Golf ball", 50);
        orderService.createOrder(order.getItemGroups(),order.getCustomerID());
        assertTrue(orderService.getAllOrders().stream()
                .map(Order::getCustomerID).collect(Collectors.toList())
                .contains(order.getCustomerID()));
    }


    @Test
    void whenCreatingOrderOf50GolfBalls_totalPriceOfOrderIs50() {
        Order order = prepareSingleLineOrder("Golf ball", 50);
        Order loggedOrder = orderService.createOrder(order.getItemGroups(),order.getCustomerID());
        assertEquals(50, loggedOrder.getTotalPrice());
    }

    @Test
    void whenCreatingOrderWithItemThatDoesntExist_exceptionIsThrown() {
        Order wrongOrder = prepareSingleLineOrder("Non-existing Golf ball", 50);
        Assertions.assertThatThrownBy(() -> orderService.createOrder(wrongOrder.getItemGroups(),wrongOrder.getCustomerID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No item found with this id.");
    }

    @Test
    void whenCreatingOrderWithEnoughStockAvailable_ShippingDateIsNextDay() {
        Order order = prepareSingleLineOrder("Golf ball", 50);
        orderService.createOrder(order.getItemGroups(),order.getCustomerID());
        assertEquals(orderService.getAllOrders().get(0)
                .getItemGroups().get(0)
                .getShippingDate(), LocalDate.now().plus(1, ChronoUnit.DAYS));

    }

    @Test
    void whenCreatingOrderWithNotEnoughStockAvailable_ShippingDateIsNextWeek() {
        Order newOrder = prepareSingleLineOrder("Golf ball", 60);

        orderService.createOrder(newOrder.getItemGroups(),newOrder.getCustomerID());
        assertEquals(orderService.getAllOrders().get(0)
                .getItemGroups().get(0)
                .getShippingDate(), LocalDate.now().plus(7, ChronoUnit.DAYS));
    }

    @Test
    void whenCreatingOrderWithNonExistingCustomer_thenExceptionIsThrown() {
        Order orderWithNotExistingCustomer = new Order(
                List.of(new ItemGroup("Golf ball", 50)),
                "FakeCustomerID");

        Assertions.assertThatThrownBy(() -> orderService.createOrder(
                orderWithNotExistingCustomer.getItemGroups(),orderWithNotExistingCustomer.getCustomerID()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No customer found with this id.");
    }

    @Test
    void whenPlacingOrder_itemNameIsAddedToItemGroups() {
        Order order = prepareSingleLineOrder("Golf ball", 10);
        orderService.createOrder(order.getItemGroups(),order.getCustomerID());
        assertEquals(orderService.getAllOrders().get(0)
                .getItemGroups().get(0).getItemName(), "Golf ball");
    }

    @Test
    void whenGettingOrderReportForCustomer_printOutIsReturned() {
        Order loggedOrder = createLoggedOrder();
        String orderSummary = orderService.createOrderSummary(loggedOrder);

        assertTrue(orderSummary.contains("Total price for order is: 30.0"));
        assertTrue(orderSummary.contains("Order with id: " + loggedOrder.getOrderID()));
    }

    @Test
    void whenReordering_orderIsRecreated() {
        Order loggedOrder = createLoggedOrder();
        Order reorderedOrder = orderService.reorder(loggedOrder.getOrderID());
        assertEquals(reorderedOrder.getCustomerID(), loggedOrder.getCustomerID());
        assertEquals(reorderedOrder.getTotalPrice(), loggedOrder.getTotalPrice());
        assertNotEquals(reorderedOrder.getOrderID(), loggedOrder.getOrderID());
    }

    @Test
    void whenReorderingOrderWhereThirdItemGroupWillBeOutOfStock_OnlyThirdItemGroupHasShippingDateOfNextWeek() {
        Order loggedOrder = createLoggedOrder();
        Order reorderedOrder = orderService.reorder(loggedOrder.getOrderID());

        assertEquals(reorderedOrder.getItemGroups().get(0).getShippingDate(), LocalDate.now().plus(1, ChronoUnit.DAYS));
        assertEquals(reorderedOrder.getItemGroups().get(1).getShippingDate(), LocalDate.now().plus(1, ChronoUnit.DAYS));
        assertEquals(reorderedOrder.getItemGroups().get(2).getShippingDate(), LocalDate.now().plus(7, ChronoUnit.DAYS));
    }

    @Test
    void givenUpdatedItem_whenReorderingOrder_orderUsesNewItemInformation() {
        Order newOrder = prepareOrderOfThirtyGolfBallsInThreeLines();

        Order loggedOrder = orderService.createOrder(newOrder.getItemGroups(),newOrder.getCustomerID());
        updateGolfBallItem();
        Order reorderedOrder = orderService.reorder(loggedOrder.getOrderID());

        assertEquals(reorderedOrder.getTotalPrice(), 60);
        assertTrue(reorderedOrder.getItemGroups()
                .stream()
                .map(ItemGroup::getItemName)
                .collect(Collectors.toList()).contains("Updated golf ball"));
    }

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
        return orderService.createOrder(order.getItemGroups(),order.getCustomerID());
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

    private Order prepareSingleLineOrder(String itemID, int amount) {
        return new Order(
                List.of(new ItemGroup(itemID, amount)),
                customerService.getAllCustomers().get(0).getCustomerID());
    }
}