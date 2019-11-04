package com.simon.eurder.api.customer;

public class CustomerAddressDto {

    private String streetName, streetNumber, postalCode, city;


    public CustomerAddressDto withStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public CustomerAddressDto withStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public CustomerAddressDto withPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public CustomerAddressDto withCity(String city) {
        this.city = city;
        return this;
    }
}
