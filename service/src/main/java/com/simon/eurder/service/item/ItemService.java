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
}
