package com.simon.eurder.api.item;

public class CreateItemDto {

    private String name, description;
    private String priceInEuro;
    private String amountInStock;


    public CreateItemDto withName(String name) {
        this.name = name;
        return this;
    }

    public CreateItemDto withDescription(String description) {
        this.description = description;
        return this;
    }

    public CreateItemDto withPriceInEuro(String priceInEuro) {
        this.priceInEuro = priceInEuro;
        return this;
    }

    public CreateItemDto withAmountInStock(String amountInStock) {
        this.amountInStock = amountInStock;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPriceInEuro() {
        return priceInEuro;
    }

    public String getAmountInStock() {
        return amountInStock;
    }
}
