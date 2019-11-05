package com.simon.eurder.service.item;

import com.simon.eurder.domain.item.Item;
import com.simon.eurder.domain.item.ItemRepository;
import org.springframework.stereotype.Component;

@Component
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public void createItem(Item itemToCreate) {
        itemRepository.createItem(itemToCreate);
    }

    public void updateItem(String itemID,Item updatedItem) {
        if (!itemExists(itemID)) throw new IllegalArgumentException("This item does not exist!");
        itemRepository.updateItem(itemID,updatedItem);
    }


    public Item getItemByID(String itemID) {
        return itemRepository.getItemByID(itemID);
    }


    public boolean isItemInStock(String itemID, int amount) {

        return amount <= getItemByID(itemID).getAmountInStock();

    }

    public boolean itemExists(String itemID) {
        return getItemByID(itemID) != null;
    }
}
