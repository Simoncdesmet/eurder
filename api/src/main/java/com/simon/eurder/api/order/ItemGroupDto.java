package com.simon.eurder.api.order;

public class ItemGroupDto {

    private String itemID;
    private int amount;

    public String getItemID() {
        return itemID;
    }

    public int getAmount() {
        return amount;
    }

    public ItemGroupDto withItemID(String itemID) {
        this.itemID = itemID;
        return this;
    }

    public ItemGroupDto withAmount(int amount) {
        this.amount = amount;
        return this;
    }


}
