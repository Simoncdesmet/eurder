package com.simon.eurder.api.item;

import com.simon.eurder.domain.item.Item;
import com.simon.eurder.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "api/v1/items")
@RestController
public class ItemController {

    private final ItemService itemService;
    private final ItemToDtoMapper itemToDtoMapper;

    @Autowired
    public ItemController(ItemService itemService, ItemToDtoMapper itemToDtoMapper) {
        this.itemService = itemService;
        this.itemToDtoMapper = itemToDtoMapper;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody CreateItemDto createItemDto) {
        Item createdItem = itemToDtoMapper.createItemDtoToItem(createItemDto);
        itemService.createItem(createdItem);
        return itemToDtoMapper.itemToDto(createdItem);
    }

}
