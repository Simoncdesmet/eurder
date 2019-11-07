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

import static org.springframework.http.HttpStatus.*;


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

    @GetMapping(produces = "application/json", value = "/customer/{customerID}")
    @ResponseStatus(OK)
    public String getAllOrdersByCustomerID(@PathVariable("customerID") String customerID) {
        return orderService.getOrderReportForCustomerID(customerID);
    }

    @PostMapping(consumes = "application/json", produces = "application/json", value = "/{customerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@PathVariable("customerId") String customerID, @RequestBody ItemGroupDtoWrapper itemGroupDtos) {
        Order createdOrder = createOrderBasedOnPostRequest(customerID, itemGroupDtos);
        return orderService.createOrderSummary(createdOrder);
    }

    @PostMapping(produces = "application/json", value = "reorder/{orderID}")
    @ResponseStatus(CREATED)
    public String reorderOrder(@PathVariable("orderID") String orderID) {
        Order reorderedOrder = orderService.reorder(orderID);
        return orderService.createOrderSummary(reorderedOrder);
    }


    private Order createOrderBasedOnPostRequest(String customerID, ItemGroupDtoWrapper itemGroupDtos) {
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
