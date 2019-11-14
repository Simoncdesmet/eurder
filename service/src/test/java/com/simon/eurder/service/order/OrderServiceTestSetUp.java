package com.simon.eurder.service.order;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.customer.CustomerAddress;
import com.simon.eurder.domain.customer.CustomerDBRepository;
import com.simon.eurder.domain.item.Item;
import com.simon.eurder.domain.item.ItemRepository;
import com.simon.eurder.domain.order.OrderRepository;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.service.item.ItemService;

public class OrderServiceTestSetUp {

    public OrderService setUpOrderService(ItemService itemService, CustomerService customerService) {
        return new OrderService(
                new OrderRepository(),
                new OrderValidator(itemService, customerService),
                new OrderPriceCalculator(itemService),
                itemService,
                new OrderReportService());
    }

    public CustomerService createCustomerServiceWithSimonDesmetInRepository() {
        CustomerService customerService = new CustomerService(new CustomerDBRepository());
        customerService.createCustomer(createSimonDesmet());
        return customerService;
    }

    public ItemService createItemServiceWithGolfBallInRepository() {
        ItemService itemService = new ItemService(new ItemRepository());
        itemService.createItem(createGolfBall());
        return itemService;
    }

    private Customer createSimonDesmet() {
        CustomerAddress customerAddress = new CustomerAddress(
                "Leeuwerikenstraat",
                "101/3",
                "3001",
                "Heverlee");

        return new Customer(
                "Test001",
                "Simon",
                "Desmet",
                "simoncdesmetgmail.com",
                "0487/57.70.40",
                customerAddress);
    }

    private Item createGolfBall() {
        return new Item(
                "Golf ball",
                "Golf ball",
                "A golf ball",
                1,
                50);
    }

}
