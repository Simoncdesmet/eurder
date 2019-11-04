package com.simon.eurder.service.item;

import com.simon.eurder.domain.item.Item;
import com.simon.eurder.domain.item.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemServiceTest {

    private ItemRepository itemRepository = new ItemRepository();
    private ItemService itemService = new ItemService(itemRepository);
    private Item itemToCreate;

    @BeforeEach
    void setUp() {
        itemToCreate = new Item("Golfball", "A normal golf ball", 1, 50);
    }

    @Test
    void whenAddingItem_itemIsInRepository() {
        itemService.createItem(itemToCreate);

        Assertions.assertTrue(itemRepository.getAllItems().contains(itemToCreate));

    }
}