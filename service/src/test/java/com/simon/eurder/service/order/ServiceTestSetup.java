package com.simon.eurder.service.order;

import com.simon.eurder.repository.OrderRepository;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.service.item.ItemService;

public class ServiceTestSetup {

    public OrderService setUpOrderService(ItemService itemService, CustomerService customerService) {
        return new OrderService(
                new OrderRepository(),
                new OrderValidator(itemService, customerService),
                new OrderPriceCalculator(itemService),
                itemService,
                new OrderReportService());
    }






}
