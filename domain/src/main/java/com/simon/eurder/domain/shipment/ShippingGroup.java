package com.simon.eurder.domain.shipment;

import com.simon.eurder.domain.customer.CustomerAddress;
import com.simon.eurder.domain.order.ItemGroup;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShippingGroup {

    private String shippingGroupID;
    private List<ItemGroup> itemsToShip;
    private CustomerAddress destinationAddress;
    private LocalDate shippingDate;
    private String orderID;

    public ShippingGroup() {
        this.shippingGroupID = UUID.randomUUID().toString();
    }

    public ShippingGroup withItemsToShip(List<ItemGroup> itemsToShip) {
        this.itemsToShip = itemsToShip;
        return this;
    }

    public ShippingGroup withDestinationAddress(CustomerAddress destinationAddress) {
        this.destinationAddress = destinationAddress;
        return this;
    }

    public ShippingGroup withShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
        return this;
    }

    public ShippingGroup withOrderID(String orderID) {
        this.orderID = orderID;
        return this;
    }

    public List<ItemGroup> getItemsToShip() {
        return itemsToShip;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("-----------------------")
                .append("\r\n")
                .append("Shipment (")
                .append(shippingGroupID).append(") related to order").append(orderID).append(":").append("\r\n")
                .append(itemsToShip.stream().map(ItemGroup::toString).collect(Collectors.joining("\r\n")))
                .append("Destination address: ")
                .append(destinationAddress.toString()).toString();
    }
}
