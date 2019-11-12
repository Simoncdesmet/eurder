package com.simon.eurder.service.item;

import com.simon.eurder.domain.item.Item;
import com.simon.eurder.domain.item.ItemRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public void createItem(Item itemToCreate) {

        itemRepository.createItem(itemToCreate);
    }

    public void updateItem(String itemID, Item updatedItem) {
        if (!itemExists(itemID)) throw new IllegalArgumentException("This item does not exist!");
        itemRepository.updateItem(itemID, updatedItem);
    }


    public Item getItemByID(String itemID) {
        return itemRepository.getItemByID(itemID);
    }


    public LocalDate calculateShippingDate(String itemID, int amountRequired) {
        int availableStock = getAmountInStock(itemID);
        if (amountRequired <= availableStock) {
            updateStock(itemID, availableStock - amountRequired);
            return tomorrow();
        }
        updateStock(itemID, 0);
        addReorderAmount(itemID, amountRequired - availableStock);
        return nextWeek();
    }

    private LocalDate nextWeek() {
        return LocalDate.now().plus(7, ChronoUnit.DAYS);
    }

    private LocalDate tomorrow() {
        return LocalDate.now().plus(1, ChronoUnit.DAYS);
    }

    private void addReorderAmount(String itemID, int reorderAmountRequired) {
        getItemByID(itemID).addReorderAmount(reorderAmountRequired);
    }

    private int getAmountInStock(String itemID) {
        return getItemByID(itemID).getAmountInStock();
    }

    private void updateStock(String itemID, int newAmount) {
        getItemByID(itemID).setAmountInStock(newAmount);
    }

    public boolean itemExists(String itemID) {
        return getItemByID(itemID) != null;
    }

    public void clearItems() {
        itemRepository.clearItems();
    }
}
