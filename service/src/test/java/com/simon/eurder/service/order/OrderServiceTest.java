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

        ItemService itemService = new ItemService(new ItemRepository());
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
                new ShippinDateCalculator(itemService));

        order = new Order(
                List.of(new ItemGroup("Golf ball", 50)),
                customer.getCustomerID());
    }

    @Test
    void whenCreatingOrderThroughService_orderForCustomerIDIsInRepository() {
        orderService.createOrder(order);
        assertTrue(orderRepository.getAllOrders().stream()
                .map(Order::getCustomerID).collect(Collectors.toList())
                .contains(order.getCustomerID()));
    }


    @Test
    void whenCreatingOrderOf50GolfBalls_totalPriceOfOrderIs50() {

        assertEquals(50, orderService.createOrder(order));
    }

    @Test
    void whenCreatingOrderWithItemThatDoesntExist_exceptionIsThrown() {

        order = new Order(
                List.of(new ItemGroup("Non-existing Golf ball", 50)),
                customer.getCustomerID());

        Assertions.assertThatThrownBy(() -> orderService.createOrder(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No item found with this id.");

    }

    @Test
    void whenCreatingOrderWithEnoughStockAvailable_ShippingDateIsNextDay() {

        orderService.createOrder(order);
        assertEquals(orderRepository.getAllOrders().get(0)
                .getItemGroups().get(0)
                .getShippingDate(), LocalDate.now().plus(1, ChronoUnit.DAYS));

    }

    @Test
    void whenCreatingOrderWithNotEnoughStockAvailable_ShippingDateIsNextWeek() {
        Order newOrder = new Order(
                List.of(new ItemGroup("Golf ball", 60)),
                customer.getCustomerID());

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
}