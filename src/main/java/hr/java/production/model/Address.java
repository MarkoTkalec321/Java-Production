package hr.java.production.model;

import hr.java.production.enums.Cities;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Sets and gets the address of a factory.
 */
public class Address implements Serializable {


    private String street, houseNumber, city, postalCode;
    private long id;
    //private Cities city, postalCode;

    /*public Address() {}
    public Address(String street, String houseNumber, String city, String postalCode, long id) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.id = id;
    }*/

    public void setCity(String city) {
        this.city = city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(Cities city1) {
        this.city = city1.getCityName();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Cities postalCode1) {
        this.postalCode = postalCode1.getPostlaCode();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        Optional<Object> checked = Optional.ofNullable(o);
        if (checked.isEmpty() || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) && Objects.equals(houseNumber, address.houseNumber) && Objects.equals(city, address.city) && Objects.equals(postalCode, address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, houseNumber, city, postalCode);
    }
}
