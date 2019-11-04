package com.simon.eurder.domain.item;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRepository {


    private final List<Item> items = new ArrayList<>();


    public List<Item> getAllItems() {
        return items;
    }

    public void createItem(Item itemToCreate) {
        items.add(itemToCreate);
    }
}
