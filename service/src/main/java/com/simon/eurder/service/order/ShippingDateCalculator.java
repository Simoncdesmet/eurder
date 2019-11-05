package com.simon.eurder.service.order;

import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import com.simon.eurder.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class ShippingDateCalculator {

    private final ItemService itemService;

    @Autowired
    public ShippingDateCalculator(ItemService itemService) {
        this.itemService = itemService;
    }



    public LocalDate calculateShippingDate(ItemGroup itemGroup) {
        if (itemService.isItemInStock(itemGroup.getItemID(), itemGroup.getAmount())) {
            return LocalDate.now().plus(1, ChronoUnit.DAYS);
        }
        return LocalDate.now().plus(7, ChronoUnit.DAYS);
    }
}
