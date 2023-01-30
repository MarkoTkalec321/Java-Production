package hr.java.production.model;

import hr.java.production.enums.Cities;

/**
 * Takes parameters of address and calls build to set the address.
 */
public class BuilderPattern {

    //private String street, houseNumber, city, postalCode;
    private String street, houseNumber;
    //private Cities city, postalCode;
    private String city, postalCode;
    private long id;

    public BuilderPattern id(long id){
        this.id = id;
        return this;
    }

    /**
     * Sets the street parameter.
     *
     * @param street Type of String.
     * @return Returns the street parameter.
     */
    public BuilderPattern street(String street) {
        this.street = street;
        return this;
    }

    /**
     * Sets the house number parameter.
     *
     * @param houseNumber Type of String.
     * @return Returns the house number parameter.
     */
    public BuilderPattern house(String houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    /**
     * Sets the city parameter.
     * //@param city Type of String.
     *
     * @return Returns the city parameter.
     */
    public BuilderPattern city(String city1) {
        //this.city = city;
        //this.city = city;
        this.city = city1;
        return this;
    }

    /**
     * Sets the postal code parameter.
     * //@param postalCode Type of String.
     *
     * @return Returns the code parameter.
     */
    public BuilderPattern code(String postalCode1) {
        //this.postalCode = postalCode;
        this.postalCode = postalCode1;
        return this;
    }

    /**
     * Sets the parameters of address when called.
     *
     * @return Returns address type parameter.
     */
    public Address build() {

        Address address = new Address();

        address.setId(this.id);
        address.setStreet(this.street);
        address.setHouseNumber(this.houseNumber);
        address.setCity(this.city);
        address.setPostalCode(this.postalCode);

        return address;
    }

}
