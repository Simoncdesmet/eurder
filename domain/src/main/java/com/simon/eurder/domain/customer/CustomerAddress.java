package com.simon.eurder.domain.customer;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class CustomerAddress {

    @Column(name = "STREETNAME")
    private String streetName;
    @Column(name = "STREETNUMBER")
    private String streetNumber;
    @Column(name = "POSTALCODE")
    private String postalCode;
    @Column(name = "CITY")
    private String city;

    private CustomerAddress() {
    }

    public CustomerAddress(String streetName, String streetNumber, String postalCode, String city) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.city = city;
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

    @Override
    public String toString() {
        return streetName+ " "+streetNumber+"\r\n"+
                postalCode+", "+city;
    }
}
