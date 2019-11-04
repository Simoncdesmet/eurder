package com.simon.eurder.api.item;

public class CreateItemDto {

    private String name, description;
    private double priceInEuro;
    private int amountInStock;


    public CreateItemDto withName(String name) {
        this.name = name;
        return this;
    }

    public CreateItemDto withDescription(String description) {
        this.description = description;
        return this;
    }

    public CreateItemDto withPriceInEuro(double priceInEuro) {
        this.priceInEuro = priceInEuro;
        return this;
    }

    public CreateItemDto withAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
        return this;
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
