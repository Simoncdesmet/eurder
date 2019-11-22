package com.simon.eurder.service.item;

import com.simon.eurder.domain.item.Item;
import com.simon.eurder.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;
    private Item itemToCreate;

    @BeforeEach
    void setUp() {
        itemToCreate = new Item("Golf ball", "A normal golf ball", 1, 50);
    }

    @Test
    void whenAddingItem_itemIsInRepository() {
        itemService.createItem(itemToCreate);
        assertTrue(itemRepository
                .findAll().stream().map(Item::getName).collect(Collectors.toList())
                .contains("Golf ball"));
    }


    @Sql(scripts = {"classpath:delete-rows.sql","classpath:create-item.sql"})
    @Test
    void whenGettingAllItems_golfBallIsInRepository() {
        assertTrue(itemRepository
                .findAll().stream().map(Item::getName).collect(Collectors.toList())
                .contains("Golf ball"));
    }
}