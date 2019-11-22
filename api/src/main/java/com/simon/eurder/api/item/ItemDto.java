package com.simon.eurder.api.item;

public class ItemDto {


    private String externalID;
    private String name, description;
    private double priceInEuro;
    private int amountInStock;


    public ItemDto withexternalID(String itemID) {
        this.externalID = itemID;
        return this;
    }

    public ItemDto withName(String name) {
        this.name = name;
        return this;
    }

    public ItemDto withDescription(String description) {
        this.description = description;
        return this;
    }

    public ItemDto withPriceInEuro(double priceInEuro) {
        this.priceInEuro = priceInEuro;
        return this;
    }

    public ItemDto withAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
        return this;
    }

    public String getExternalID() {
        return externalID;
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
