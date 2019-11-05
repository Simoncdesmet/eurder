package com.simon.eurder.api.item;

import com.simon.eurder.api.customer.CustomerController;
import com.simon.eurder.domain.item.Item;
import com.simon.eurder.service.item.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RequestMapping(path = "api/v1/items")
@RestController
public class ItemController {

    private final ItemService itemService;
    private final ItemToDtoMapper itemToDtoMapper;
    private final ItemInputValidator itemInputValidator;
    private final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    public ItemController(ItemService itemService, ItemToDtoMapper itemToDtoMapper, ItemInputValidator itemInputValidator) {
        this.itemService = itemService;
        this.itemToDtoMapper = itemToDtoMapper;
        this.itemInputValidator = itemInputValidator;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody CreateItemDto createItemDto) {
        logger.info("Validating item input...");
        itemInputValidator.validateDtoInput(createItemDto);
        logger.info("Creating item...");
        Item createdItem = itemToDtoMapper.createItemDtoToItem(createItemDto);
        logger.info("Adding item to repository...");
        itemService.createItem(createdItem);
        logger.info("Returning item...");
        return itemToDtoMapper.itemToDto(createdItem);
    }

    @PutMapping(consumes = "application/json", produces = "application/json", value = "/{itemID}")
    @ResponseStatus(CREATED)
    public ItemDto updateItem(@PathVariable("itemID") String itemID, @RequestBody CreateItemDto createItemDto) {
        logger.info("Validating item input...");
        itemInputValidator.validateDtoInput(createItemDto);
        Item updatedItem = itemToDtoMapper.createItemDtoToItem(createItemDto);
        logger.info("Updating item...");
        itemService.updateItem(itemID, updatedItem);
        logger.info("Returning item...");
        return itemToDtoMapper.itemToDto(updatedItem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleInvalidInput(IllegalArgumentException exception, HttpServletResponse response) throws IOException {
        response.sendError(BAD_REQUEST.value(), exception.getMessage());
    }

}
