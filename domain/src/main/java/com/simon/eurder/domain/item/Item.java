package com.simon.eurder.domain.item;

import java.util.UUID;

public class Item {

    private String itemID;
    private String name, description;
    private double priceInEuro;
    private int amountInStock;
    private int reorderAmount;

    public Item(String name, String description, double priceInEuro, int amountInStock) {
        this(UUID.randomUUID().toString(), name, description, priceInEuro, amountInStock);
    }

    public Item(String itemID, String name, String description, double priceInEuro, int amountInStock) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.priceInEuro = priceInEuro;
        this.amountInStock = amountInStock;
        this.reorderAmount = 0;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemID() {
        return itemID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPriceInEuro() {
        return priceInEuro;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void addReorderAmount(int reorderAmountRequired) {
        this.reorderAmount += reorderAmountRequired;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    public int getReorderAmount() {
        return this.reorderAmount;
    }
}
