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

    private String getOrderPrintOut(Order order) {
        return new StringBuilder()
                .append("----------------------")
                .append("\r\n")
                .append("Order ")
                .append(order.getOrderID())
                .append("\r\n")
                .append(getItemPrintOutForOrder(order))
                .append("\r\n")
                .append("----------------------")
                .toString();
    }

    private String getItemPrintOutForOrder(Order order) {
        return order.getItemGroups().stream()
                .map(ItemGroup::toString)
                .collect(Collectors.joining("\r\n"));
    }

}
