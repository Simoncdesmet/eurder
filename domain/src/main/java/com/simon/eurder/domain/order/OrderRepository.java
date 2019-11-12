package com.simon.eurder.domain.order;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderRepository {

    private List<Order> orders;

    public OrderRepository() {
        this.orders = new ArrayList<>();
    }

    public void createOrder(Order order) {

        this.orders.add(order);
    }

    public List<Order> getAllOrders() {

        return orders;
    }

    public List<Order> getAllOrdersByCustomerId(String customerID) {
        return orders.stream()
                .filter(order->order.getCustomerID().equals(customerID))
                .collect(Collectors.toList());
    }

    public Optional<Order> getOrderByOrderID(String orderID) {
        return orders.stream()
                .filter(order -> order.getOrderID().equals(orderID))
                .findFirst();
    }

    public void clearOrders() {
        orders = new ArrayList<>();
    }
}
