package com.simon.eurder.api.item;

import com.simon.eurder.domain.item.Item;
import com.simon.eurder.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequestMapping(path = "api/v1/items")
@RestController
public class ItemController {

    private final ItemService itemService;
    private final ItemToDtoMapper itemToDtoMapper;
    private final ItemInputValidator itemInputValidator;

    @Autowired
    public ItemController(ItemService itemService, ItemToDtoMapper itemToDtoMapper, ItemInputValidator itemInputValidator) {
        this.itemService = itemService;
        this.itemToDtoMapper = itemToDtoMapper;
        this.itemInputValidator = itemInputValidator;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody CreateItemDto createItemDto) {
        itemInputValidator.validateInput(createItemDto);
        Item createdItem = itemToDtoMapper.createItemDtoToItem(createItemDto);
        itemService.createItem(createdItem);
        return itemToDtoMapper.itemToDto(createdItem);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public void handleInvalidInput(IllegalArgumentException exception, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), exception.getMessage());
    }

}
