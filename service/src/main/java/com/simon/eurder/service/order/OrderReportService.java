package com.simon.eurder.service.order;

import com.simon.eurder.domain.order.ItemGroup;
import com.simon.eurder.domain.order.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
public class OrderReportService {


    public String displayOrderReport(List<Order> ordersFromCustomer) {
        StringJoiner reportJoiner = new StringJoiner("\r\n");
        reportJoiner.add(ordersFromCustomer.stream()
                .map(this::getOrderPrintOut)
                .collect(Collectors.joining("\r\n" + "\r\n")));

        return reportJoiner.toString();
    }

    public String getOrderPrintOut(Order order) {
        StringJoiner orderPrintout = new StringJoiner("\r\n", "-----------------", "-----------------");
        orderPrintout.add("\r\n"+"Order with id: " + order.getExternalId());
        orderPrintout.add(getItemPrintOutForOrder(order));
        orderPrintout.add(displayOrderTotal(order)+"\r\n");
        return orderPrintout.toString();
    }

    private String displayOrderTotal(Order order) {
        return "Total price for order is: " + order.getTotalPrice();
    }

    private String getItemPrintOutForOrder(Order order) {
        return order.getItemGroups().stream()
                .map(ItemGroup::toString)
                .collect(Collectors.joining("\r\n"));
    }

}
