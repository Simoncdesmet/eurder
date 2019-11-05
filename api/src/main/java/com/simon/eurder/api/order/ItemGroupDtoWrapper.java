package com.simon.eurder.api.order;

import com.simon.eurder.domain.order.ItemGroup;

import java.util.ArrayList;
import java.util.List;

public class ItemGroupDtoWrapper {

    private List<ItemGroupDto> itemGroupDtos;


    public ItemGroupDtoWrapper() {
        this.itemGroupDtos = new ArrayList<>();
    }

    public List<ItemGroupDto> getItemGroups() {
        return itemGroupDtos;
    }

    public void setItemGroupDtos(List<ItemGroupDto> itemGroupDtos) {
        this.itemGroupDtos = itemGroupDtos;
    }

    public ItemGroupDtoWrapper withItemGroupDto(ItemGroupDto itemGroupDto) {
        itemGroupDtos.add(itemGroupDto);
        return this;
    }


}
