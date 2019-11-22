package com.simon.eurder.domain.order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 1)
    private long internalID;

    @Column(name = "EXTERNAL_ID")
    private String externalId;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID")
    private List<ItemGroup> itemGroups = new ArrayList<>();

    @Column(name = "TOTAL_PRICE")
    private double totalPrice;

    @Column(name = "CUSTOMER_ID")
    private String customerID;



    public Order() {
    }

    public Order(List<ItemGroup> itemGroups, String customerID) {
        this.externalId = UUID.randomUUID().toString();
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

    public String getExternalId() {
        return externalId;
    }


}
