package com.simon.eurder.domain.item;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ITEM")
public class Item {



    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq")
    @SequenceGenerator(name = "item_seq", sequenceName = "item_seq", allocationSize = 1)
    private long itemID;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE_IN_EURO")
    private double priceInEuro;

    @Column(name = "AMOUNT_IN_STOCK")
    private int amountInStock;

    @Column(name = "AMOUNT_TO_REORDER")
    private int reorderAmount;

    @Column(name = "EXTERNAL_ID")
    private String externalId;

    public Item() {
    }


    public Item(String name, String description, double priceInEuro, int amountInStock) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.priceInEuro = priceInEuro;
        this.amountInStock = amountInStock;
        this.reorderAmount = 0;
        this.externalId = UUID.randomUUID().toString();
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public long getItemID() {
        return itemID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPriceInEuro(double priceInEuro) {
        this.priceInEuro = priceInEuro;
    }

    public double getPriceInEuro() {
        return priceInEuro;
    }


    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void addReorderAmount(int reorderAmountRequired) {
        this.reorderAmount += reorderAmountRequired;
    }

    public int getReorderAmount() {
        return this.reorderAmount;
    }


    public String getExternalId() {
        return externalId;
    }
}
