package com.simon.eurder.service.order;

import com.simon.eurder.domain.customer.Customer;
import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import com.simon.eurder.domain.order.OrderRepository;
import com.simon.eurder.service.customer.CustomerService;
import com.simon.eurder.service.item.ItemService;

import java.util.List;
import java.util.stream.Collectors;

public class OrderValidator {

    private final ItemService itemService;
    private final CustomerService customerService;

    public OrderValidator(ItemService itemService, CustomerService customerService) {
        this.itemService = itemService;
        this.customerService = customerService;
    }

    public void validateOrder(Order order) {
        validateItemIDs(order.getItemGroups());
        validateCustomer(order.getCustomerID());
    }

    private void validateCustomer(String customerID) {
        if (!customerService.getAllCustomers()
                .stream()
                .map(Customer::getCustomerID)
                .collect(Collectors.toList())
                .contains(customerID))
            throw new IllegalArgumentException("No customer found with this id.");
    }

    private void validateItemIDs(List<ItemGroup> itemGroups) {
        itemGroups
                .forEach(this::validateItemID);
    }

    private void validateItemID(ItemGroup itemGroup) {
        if (itemService.getItemByID(itemGroup.getItemID()) == null)
            throw new IllegalArgumentException("No item found with this id.");
    }
}
