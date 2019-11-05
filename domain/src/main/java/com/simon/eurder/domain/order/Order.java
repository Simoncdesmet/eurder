package com.simon.eurder.domain.order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {

    private String orderID;
    private List<ItemGroup> itemGroups;
    private double totalPrice;
    private String customerID;


    public Order(List<ItemGroup> itemGroups,String customerID) {
        this.orderID = UUID.randomUUID().toString();
        this.itemGroups = itemGroups;
        this.customerID = customerID;
    }

    public void setTotalPrice(double orderPrice) {
        this.totalPrice = orderPrice;
    }

    public double getTotalPrice() {
    return totalPrice;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public String getCustomerID() {
        return customerID;
    }



    private void calculateShippingDate(ItemGroup itemGroup) {
    }


}
