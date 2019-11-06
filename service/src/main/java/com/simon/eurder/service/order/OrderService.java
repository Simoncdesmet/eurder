package com.simon.eurder.service.order;

import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import com.simon.eurder.domain.order.OrderRepository;
import com.simon.eurder.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderService {


    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderPriceCalculator orderPriceCalculator;
    private final ShippingDateCalculator shippingDateCalculator;
    private final ItemService itemService;
    private final OrderReportService orderReportService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator, OrderPriceCalculator orderPriceCalculator, ShippingDateCalculator shippingDateCalculator, ItemService itemService, OrderReportService orderReportService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderPriceCalculator = orderPriceCalculator;
        this.shippingDateCalculator = shippingDateCalculator;
        this.itemService = itemService;
        this.orderReportService = orderReportService;
    }

    public Order createOrder(Order order) {

        return createOrder(
                order.getItemGroups().stream()
                        .map(itemGroup -> new ItemGroup(itemGroup.getItemID(), itemGroup.getAmount()))
                        .collect(Collectors.toList()),
                order.getCustomerID());
    }

    public Order createOrder(List<ItemGroup> itemGroups, String customerID) {
        Order order = new Order(itemGroups, customerID);
        orderValidator.validateOrder(order);
        setItemNames(order);
        setTotalPrice(order);
        setShippingDates(order);
        orderRepository.createOrder(order);
        return order;

    }

    private void setItemNames(Order order) {
        order.getItemGroups()
                .forEach(itemGroup -> itemGroup
                        .setItemName(itemService.getItemByID(itemGroup.getItemID())
                                .getName()));
    }

    private void setTotalPrice(Order order) {
        order.setTotalPrice(orderPriceCalculator.calculateOrderPrice(order));
    }

    public void setShippingDates(Order order) {
        order.getItemGroups()
                .forEach(itemGroup ->
                        itemGroup.setShippingDate(shippingDateCalculator.calculateShippingDate(itemGroup)));
    }


    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public String getOrderReportForCustomerID(String customerID) {
        return orderReportService.displayOrderReport(orderRepository.getAllOrdersByCustomerId(customerID));
    }

    public String createOrderSummary(Order order) {
        return orderReportService.getOrderPrintOut(order);
    }

    public Order reorder(String orderID) {
        Order oldOrder = orderRepository.getOrderByOrderID(orderID).orElseThrow(
                () -> new NoSuchElementException("No order found with this ID."));

        return createOrder(oldOrder);
    }
}
