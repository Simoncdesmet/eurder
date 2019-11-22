package com.simon.eurder.domain.item;

import com.simon.eurder.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;
    private Item itemToCreate;

    @BeforeEach
    void setUp() {
        itemToCreate = new Item("Golfball", "A normal golf ball", 1, 50);
    }

    @Test
    void whenCreatingItem_itemIsInRepository() {

        itemRepository.save(itemToCreate);
        assertTrue(itemRepository
                .findAll().stream().map(Item::getName).collect(Collectors.toList())
                .contains("Golfball"));
    }
}