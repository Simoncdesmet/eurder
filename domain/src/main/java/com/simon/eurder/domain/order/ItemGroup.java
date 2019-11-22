package com.simon.eurder.domain.order;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ITEM_GROUP")
public class ItemGroup {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_group_seq")
    @SequenceGenerator(name = "item_group_seq", sequenceName = "item_group_seq", allocationSize = 1)
    private long Id;

    @Column(name = "ITEM_EXTERNAL_ID")
    private String itemID;
    @Column(name = "ITEM_NAME")
    private String itemName;
    @Column(name = "AMOUNT")
    private int amount;
    @Column(name = "SHIPPINGDATE")
    private LocalDate shippingDate;
    @Column(name = "ITEM_GROUP_PRICE")
    private double itemGroupPrice;

    public ItemGroup() {
    }

    public ItemGroup(String itemID, int amount) {
        this(itemID, "", amount);
    }

    public ItemGroup(String itemId, String itemName, int amount) {
        this.itemID = itemId;
        this.itemName = itemName;
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
        return "Item " + itemID + " - name: " + itemName + "\r\n" +
                "Amount ordered: " + amount + ", for a total of " + itemGroupPrice + " euros.";
    }
}
