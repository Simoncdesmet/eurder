package com.simon.eurder.domain.item;

import java.util.UUID;

public class Item {

    private String itemID;
    private String name, description;
    private double priceInEuro;
    private int amountInStock;

    public Item(String name, String description, double priceInEuro, int amountInStock) {
        this(UUID.randomUUID().toString(),name,description,priceInEuro,amountInStock);
    }

    public Item(String itemID, String name, String description, double priceInEuro, int amountInStock) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.priceInEuro = priceInEuro;
        this.amountInStock = amountInStock;
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
}
