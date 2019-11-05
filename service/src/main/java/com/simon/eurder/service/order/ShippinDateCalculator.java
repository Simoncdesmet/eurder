package com.simon.eurder.service.order;

import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import com.simon.eurder.service.item.ItemService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ShippinDateCalculator {

    private final ItemService itemService;

    public ShippinDateCalculator(ItemService itemService) {
        this.itemService = itemService;
    }



    public LocalDate calculateShippingDate(ItemGroup itemGroup) {
        if (itemService.isItemInStock(itemGroup.getItemID(), itemGroup.getAmount())) {
            return LocalDate.now().plus(1, ChronoUnit.DAYS);
        }
        return LocalDate.now().plus(7, ChronoUnit.DAYS);
    }
}
