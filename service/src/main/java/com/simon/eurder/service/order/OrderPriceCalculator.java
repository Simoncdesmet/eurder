package com.simon.eurder.service.order;

import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import com.simon.eurder.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderPriceCalculator {

    private final ItemService itemService;

    @Autowired
    public OrderPriceCalculator(ItemService itemService) {
        this.itemService = itemService;
    }

    public double calculateOrderPrice(Order order) {
        return order.getItemGroups().stream()
                .mapToDouble(this::calculateItemGroupPrice)
                .sum();
    }

    private double calculateItemGroupPrice(ItemGroup itemGroup) {
        return itemGroup.getAmount() * itemService.getItemByID(itemGroup.getItemID()).getPriceInEuro();
    }
}
