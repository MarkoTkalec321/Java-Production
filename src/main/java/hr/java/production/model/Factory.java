package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Takes in name, address of a factory and an array type Item.
 */
public class Factory extends NamedEntity implements Serializable {

    private String name;
    private Address address;
    private Set<Item> items;
    private Long id;
    private Long addressId;


    /**
     * Takes in name, address of a factory and an array type Item.
     *
     * @param name    Type String.
     * @param address Type Address.
     * @param items   Type Item.
     */
    public Factory(String name, long id, Address address, Set<Item> items) {
        super(name, id);
        this.address = address;
        this.items = items;
    }

    public Factory(long id, String name, long addressId){
        //super(name, id);
        this.id = id;
        this.name = name;
        this.addressId = addressId;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Factory factory = (Factory) o;
        return id == factory.id && Objects.equals(name, factory.name) && Objects.equals(address, factory.address) && Objects.equals(items, factory.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, address, items, id);
    }

}
