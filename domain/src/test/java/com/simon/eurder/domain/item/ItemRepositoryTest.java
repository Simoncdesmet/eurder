package com.simon.eurder.domain.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.simon.eurder.repository.ItemRepository;

import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();
    Item itemToCreate;

    @BeforeEach
    void setUp() {
        itemToCreate = new Item("Golfball","A normal golf ball",1,50);
    }

    @Test
    void whenCreatingItem_itemIsInRepository() {

      itemRepository.createItem(itemToCreate);
        assertTrue(itemRepository.getAllItems().contains(itemToCreate));
    }
}