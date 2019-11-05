package com.simon.eurder.service.order;

import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import com.simon.eurder.domain.order.OrderRepository;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderService {


    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderPriceCalculator orderPriceCalculator;
    private final ShippinDateCalculator shippinDateCalculator;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator, OrderPriceCalculator orderPriceCalculator, ShippinDateCalculator shippinDateCalculator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderPriceCalculator = orderPriceCalculator;
        this.shippinDateCalculator = shippinDateCalculator;
    }

    public Order createOrder(Order order) {
        return createOrder(order.getItemGroups(), order.getCustomerID());
    }

    public Order createOrder(List<ItemGroup> itemGroups, String customerID) {
        Order order = new Order(itemGroups, customerID);
        orderValidator.validateOrder(order);
        setTotalPrice(order);
        setShippingDates(order);
        orderRepository.createOrder(order);
        return order;

    }

    private void setTotalPrice(Order order) {
        order.setTotalPrice(orderPriceCalculator.calculateOrderPrice(order));
    }

    public void setShippingDates(Order order) {
        order.getItemGroups()
                .forEach(itemGroup ->
                        itemGroup.setShippingDate(shippinDateCalculator.calculateShippingDate(itemGroup)));
    }


    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }
}
