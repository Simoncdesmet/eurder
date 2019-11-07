package com.simon.eurder.service.order;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerAddress;
import com.simon.eurder.domain.customer.CustomerRepository;
import com.simon.eurder.domain.item.Item;
import com.simon.eurder.domain.item.ItemRepository;
import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import com.simon.eurder.domain.order.OrderRepository;
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
    private Customer customer;
    private CustomerService customerService;
    private OrderRepository orderRepository;
    private OrderService orderService;
    private Order order;
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(new CustomerRepository());
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

        customerService.createCustomer(customer);

        itemService = new ItemService(new ItemRepository());
        Item item = new Item(
                "Golf ball",
                "Golf ball",
                "A golf ball",
                1,
                50);
        itemService.createItem(item);

        orderRepository = new OrderRepository();
        orderService = new OrderService(
                orderRepository,
                new OrderValidator(itemService, customerService),
                new OrderPriceCalculator(itemService),
                new ShippingDateCalculator(itemService), itemService, new OrderReportService());

    }

    @Test
    void whenCreatingOrderThroughService_orderForCustomerIDIsInRepository() {
        Order order = prepareSingleLineOrder("Golf ball", 50);
        orderService.createOrder(order);
        assertTrue(orderRepository.getAllOrders().stream()
                .map(Order::getCustomerID).collect(Collectors.toList())
                .contains(order.getCustomerID()));
    }


    @Test
    void whenCreatingOrderOf50GolfBalls_totalPriceOfOrderIs50() {

        Order order = prepareSingleLineOrder("Golf ball", 50);
        assertEquals(50, orderService.createOrder(order).getTotalPrice());
    }

    @Test
    void whenCreatingOrderWithItemThatDoesntExist_exceptionIsThrown() {

        Order wrongOrder = prepareSingleLineOrder("Non-existing Golf ball", 50);
        Assertions.assertThatThrownBy(() -> orderService.createOrder(wrongOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No item found with this id.");

    }

    @Test
    void whenCreatingOrderWithEnoughStockAvailable_ShippingDateIsNextDay() {

        Order order = prepareSingleLineOrder("Golf ball", 50);
        orderService.createOrder(order);
        assertEquals(orderRepository.getAllOrders().get(0)
                .getItemGroups().get(0)
                .getShippingDate(), LocalDate.now().plus(1, ChronoUnit.DAYS));

    }

    @Test
    void whenCreatingOrderWithNotEnoughStockAvailable_ShippingDateIsNextWeek() {
        Order newOrder = prepareOrderWith60GolfBallsInOneLine();

        orderService.createOrder(newOrder);
        assertEquals(orderRepository.getAllOrders().get(0)
                .getItemGroups().get(0)
                .getShippingDate(), LocalDate.now().plus(7, ChronoUnit.DAYS));
    }



    @Test
    void whenCreatingOrderWithNonExistingCustomer_thenExceptionIsThrown() {
        CustomerAddress customerAddress = new CustomerAddress(
                "Leeuwerikenstraat",
                "101/3",
                "3001",
                "Heverlee");

        Customer notExistingCustomer = new Customer(
                "Test002",
                "Simon",
                "Desmet",
                "simoncdesmetgmail.com",
                "0487/57.70.40",
                customerAddress);

        Order orderWithNotExistingCustomer = new Order(
                List.of(new ItemGroup("Golf ball", 50)),
                notExistingCustomer.getCustomerID());

        Assertions.assertThatThrownBy(() -> orderService.createOrder(orderWithNotExistingCustomer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No customer found with this id.");
    }

    @Test
    void whenPlacingOrder_amountInStockIsUpdated() {
        Order newOrder = new Order(
                List.of(new ItemGroup("Golf ball", 30)),
                customer.getCustomerID());

        orderService.createOrder(newOrder);
        assertEquals(itemService.getItemByID("Golf ball").getAmountInStock(), 20);
    }

    @Test
    void whenPlacingOrderThatExceedsStock_amountInReorderIsUpdated() {
        Order newOrder = prepareOrderWith60GolfBallsInOneLine();

        orderService.createOrder(newOrder);
        assertEquals(itemService.getItemByID("Golf ball").getReorderAmount(), 10);
        assertEquals(itemService.getItemByID("Golf ball").getAmountInStock(), 0);
    }


    @Test
    void whenPlacingMultipleOrdersThatExceedsStock_amountInReorderIsUpdated() {
        Order newOrder = prepareOrderWith60GolfBallsInOneLine();

        orderService.createOrder(newOrder);
        orderService.createOrder(newOrder);
        assertEquals(itemService.getItemByID("Golf ball").getReorderAmount(), 70);
        assertEquals(itemService.getItemByID("Golf ball").getAmountInStock(), 0);
    }

    @Test
    void whenPlacingOrder_itemNameIsAddedToItemGroups() {
        Order newOrder = prepareOrderOf10GolfBallsInOneLine();

        orderService.createOrder(newOrder);
        orderService.createOrder(newOrder);
        assertEquals(orderRepository.getAllOrders().get(0)
                .getItemGroups().get(0).getItemName(), "Golf ball");
        assertEquals(orderRepository.getAllOrders().get(1)
                .getItemGroups().get(0).getItemName(), "Golf ball");
    }

    private Order prepareOrderOf10GolfBallsInOneLine() {
        return new Order(
                    List.of(new ItemGroup("Golf ball", 10)),
                    customer.getCustomerID());
    }

    @Test
    void whenGettingOrderReportForCustomer_printOutIsReturned() {
        Order loggedOrder = createLoggedOrder();
        String orderSummary = orderService.createOrderSummary(loggedOrder);

        System.out.println(orderSummary);
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

        assertEquals(reorderedOrder.getItemGroups().get(0).getShippingDate(), LocalDate.now().plus(1,ChronoUnit.DAYS));
        assertEquals(reorderedOrder.getItemGroups().get(1).getShippingDate(), LocalDate.now().plus(1,ChronoUnit.DAYS));
        assertEquals(reorderedOrder.getItemGroups().get(2).getShippingDate(), LocalDate.now().plus(7,ChronoUnit.DAYS));
    }


    @Test
    void givenUpdatedItem_whenReorderingOrder_orderUsesNewItemInformation() {
        Order newOrder = prepareOrderOfThirtyGolfBallsInThreeLines();

        Order loggedOrder = orderService.createOrder(newOrder);
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
        Order reorderedOrder = orderService.reorder(loggedOrder.getOrderID());
        assertEquals(loggedOrder.getTotalPrice(), 30);
        assertFalse(loggedOrder.getItemGroups()
                .stream()
                .map(ItemGroup::getItemName)
                .collect(Collectors.toList()).contains("Updated golf ball"));
    }

    private Order createLoggedOrder() {
        Order newOrder = prepareOrderOfThirtyGolfBallsInThreeLines();
        return orderService.createOrder(newOrder);
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
                customer.getCustomerID());
    }

    private Order prepareOrderWith60GolfBallsInOneLine() {
        return new Order(
                List.of(new ItemGroup("Golf ball", 60)),
                customer.getCustomerID());
    }

    private Order prepareSingleLineOrder(String itemID, int amount) {
        return new Order(
                List.of(new ItemGroup(itemID, amount)),
                customer.getCustomerID());
    }


}