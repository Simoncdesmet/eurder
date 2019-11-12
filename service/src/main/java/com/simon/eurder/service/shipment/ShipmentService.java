package com.simon.eurder.service.shipment;

import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import com.simon.eurder.domain.shipment.ShippingGroup;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
public class ShipmentService {

    private final OrderService orderService;
    private final CustomerService customerService;


    @Autowired
    public ShipmentService(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    public String getShippingOverviewForDay() {
        return getShippingOverviewForDay(LocalDate.now());
    }

    public String getShippingOverviewForDay(LocalDate dateOfShipping) {
        StringJoiner joiner = new StringJoiner("\r\n");
        joiner.add("Shipment overview for: " + dateOfShipping);
        joiner.add(getShippingGroupsForDay(dateOfShipping).stream()
                .map(ShippingGroup::toString)
                .collect(Collectors.joining("\r\n")));
        return joiner.toString();
    }


    private List<ShippingGroup> getShippingGroupsForDay(LocalDate dateOfShipping) {
        return getAllOrders().stream().map(order -> createShippingGroupBasedOnOrder(order, dateOfShipping))
                .filter(shippingGroup -> !shippingGroup.getItemsToShip().isEmpty())
                .collect(Collectors.toList());
    }

    private ShippingGroup createShippingGroupBasedOnOrder(Order order, LocalDate dateOfShipping) {

        return new ShippingGroup()
                .withDestinationAddress(customerService.getCustomerByID(order.getCustomerID()).getCustomerAddress())
                .withItemsToShip(getItemGroupsForDay(order, dateOfShipping))
                .withOrderID(order.getOrderID())
                .withShippingDate(dateOfShipping);
    }


    private List<ItemGroup> getItemGroupsForDay(Order order, LocalDate localDate) {
        return order.getItemGroups().stream()
                .filter(itemGroup -> itemGroup.getShippingDate().equals(localDate))
                .collect(Collectors.toList());
    }


    private List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }


}
