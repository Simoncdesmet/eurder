package com.simon.eurder.api.order;


import com.simon.eurder.domain.order.Order;
import com.simon.eurder.service.order.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequestMapping(path = "/api/v1/orders")
@RestController
public class OrderController {


    private final OrderService orderService;
    private final ItemGroupDtoMapper itemGroupDtoMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService, ItemGroupDtoMapper itemGroupDtoMapper) {
        this.orderService = orderService;
        this.itemGroupDtoMapper = itemGroupDtoMapper;
    }

    @PostMapping(consumes = "application/json", produces = "application/json", value = "/{customerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@PathVariable("customerId") String customerID, @RequestBody ItemGroupDtoWrapper itemGroupDtos) {
        Order createdOrder = createOrderBasedOnRequest(customerID, itemGroupDtos);
        return displayOrderConfirmation(createdOrder);
    }

    private Order createOrderBasedOnRequest(String customerID, ItemGroupDtoWrapper itemGroupDtos) {
        return orderService.createOrder(itemGroupDtoMapper.mapDtosToItemGroups(itemGroupDtos), customerID);
    }

    private String displayOrderConfirmation(Order order) {
        return "Total order price: " + order.getTotalPrice();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleInvalidInput(IllegalArgumentException exception, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), exception.getMessage());
    }
}