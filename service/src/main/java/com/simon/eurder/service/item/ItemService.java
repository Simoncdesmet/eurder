package com.simon.eurder.service.item;

import com.simon.eurder.domain.item.Item;
import com.simon.eurder.repository.ItemRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public void createItem(Item itemToCreate) {

        itemRepository.save(itemToCreate);
    }

    public Item updateItem(String itemID, Item updatedItem) {
        Item foundItem = getItemByID(itemID);
        foundItem.setAmountInStock(updatedItem.getAmountInStock());
        foundItem.setName(updatedItem.getName());
        foundItem.setDescription(updatedItem.getDescription());
        foundItem.setPriceInEuro(updatedItem.getPriceInEuro());
        return foundItem;
    }


    public Item getItemByID(String itemID) {

        return itemRepository.findByExternalId(itemID).orElseThrow(
                () -> new IllegalArgumentException("This item does not exist!"));

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
        itemRepository.deleteAll();
    }
}
