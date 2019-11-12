package com.simon.eurder.domain.item;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ItemRepository {


    private Map<String, Item> itemsByID = new ConcurrentHashMap<>();


    public List<Item> getAllItems() {
        return new ArrayList<>(itemsByID.values());
    }

    public void createItem(Item itemToCreate) {
        itemsByID.put(itemToCreate.getItemID(), itemToCreate);
    }

    public Item getItemByID(String itemID) {
        return itemsByID.get(itemID);
    }

    public void updateItem(String itemID, Item item) {
        itemsByID.put(itemID, item);
    }

    public void clearItems() {
        itemsByID = new ConcurrentHashMap<>();
    }
}
