package com.simon.eurder.domain.order;

import java.time.LocalDate;

public class ItemGroup {

    private String itemID;
    private String itemName;
    private int amount;
    private LocalDate shippingDate;
    private double itemGroupPrice;

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

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemGroupPrice(double itemGroupPrice) {
        this.itemGroupPrice = itemGroupPrice;
    }

    @Override
    public String toString() {
        return "Item " + itemID + " - name: " + itemName+"\r\n"+
                "Amount ordered: "+amount+", for a total of "+itemGroupPrice+" euros.";
    }
}
