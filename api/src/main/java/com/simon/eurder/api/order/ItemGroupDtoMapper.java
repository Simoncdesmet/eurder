package com.simon.eurder.api.order;

import com.simon.eurder.domain.order.ItemGroup;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemGroupDtoMapper {

    public List<ItemGroup> mapDtosToItemGroups(ItemGroupDtoWrapper itemGroupDtos) {
        return itemGroupDtos.getItemGroups().stream()
                .map(this::mapDtoToItemGroup)
                .collect(Collectors.toList());
    }

    private ItemGroup mapDtoToItemGroup(ItemGroupDto itemGroupDto) {
        return new ItemGroup(itemGroupDto.getItemID(), itemGroupDto.getAmount());
    }

}
