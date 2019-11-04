package com.simon.eurder.api.customer;

public class CreateCustomerDto {

    private String firstName, lastName, email, phoneNumber;
    private String streetName, streetNumber, postalCode, city;

    public CreateCustomerDto withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CreateCustomerDto withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CreateCustomerDto withEmail(String email) {
        this.email = email;
        return this;
    }


    public CreateCustomerDto withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public CreateCustomerDto withStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public CreateCustomerDto withStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public CreateCustomerDto withPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public CreateCustomerDto withCity(String city) {
        this.city = city;
        return this;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }
}
