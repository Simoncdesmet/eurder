package com.simon.eurder.api.item;

import com.simon.eurder.domain.item.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemToDtoMapper {


    public Item updateItemDtoToItem(String itemID, CreateItemDto createItemDto) {
        Item updatedItem = createItemDtoToItem(createItemDto);
        updatedItem.setItemID(itemID);
        return updatedItem;
    }

    public Item createItemDtoToItem(CreateItemDto createItemDto) {
        return new Item(
                createItemDto.getName(),
                createItemDto.getDescription(),
                Double.parseDouble(createItemDto.getPriceInEuro()),
                Integer.parseInt(createItemDto.getAmountInStock()));
    }

    public ItemDto itemToDto(Item item) {
        return new ItemDto()
                .withItemID(item.getItemID())
                .withName(item.getName())
                .withDescription(item.getDescription())
                .withPriceInEuro(item.getPriceInEuro())
                .withAmountInStock(item.getAmountInStock());
    }
}
