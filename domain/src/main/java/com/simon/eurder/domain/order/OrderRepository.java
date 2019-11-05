package com.simon.eurder.domain.order;

import java.util.ArrayList;
import java.util.List;

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
}