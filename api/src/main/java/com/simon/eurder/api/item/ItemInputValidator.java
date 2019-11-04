package com.simon.eurder.api.item;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

@Component
public class ItemInputValidator {

    public void validateInput(CreateItemDto createItemDto) {
        checkForEmptyFields(createItemDto);
        validateNumber(createItemDto.getPriceInEuro());
        validateNumber(createItemDto.getAmountInStock());
    }

    private void checkForEmptyFields(CreateItemDto createItemDto) {
        if (isNull(createItemDto.getName()))
            throw new IllegalArgumentException("You need to provide a name for the item!");
        if (isNull(createItemDto.getDescription()))
            throw new IllegalArgumentException("You need to provide a last name!");
        if (isNull(createItemDto.getPriceInEuro()))
            throw new IllegalArgumentException("You need to provide a price! (can be zero)");
        if (isNull(createItemDto.getAmountInStock()))
            throw new IllegalArgumentException("You need to provide an amount! (can be zero)");
    }

    private void validateNumber(String numberToCheck) {
        if (!NumberUtils.isCreatable(numberToCheck)) throw new IllegalArgumentException("Invalid number");
        if (NumberUtils.createNumber(numberToCheck).doubleValue() < 0.0)
            throw new IllegalArgumentException("You need to provide a positive number for the price and amount!");
    }

    private boolean isNull(String stringToCheck) {
        return stringToCheck == null;
    }
}
