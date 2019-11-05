package com.simon.eurder.domain.order;

import java.time.LocalDate;

public class ItemGroup {

    private String itemID;
    private int amount;
    private LocalDate shippingDate;

    public ItemGroup(String itemID, int amount) {
        this.itemID = itemID;
        this.amount = amount;
    }

    public String getItemID() {
        return itemID;
    }

    public int getAmount() {
        return amount;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }
}
