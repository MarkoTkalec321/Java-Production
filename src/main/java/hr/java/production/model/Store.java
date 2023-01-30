package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Takes in name, webAddress and items.
 */
public class Store extends NamedEntity implements Serializable {

    private String name, webAddress;
    private Set<Item> items;
    private long id;

    /**
     * Takes in name, webAddress and items.
     *
     * @param name       Type String.
     * @param webAddress Type String.
     * @param items      Type Item.
     */
    public Store(String name, long id, String webAddress, Set<Item> items) {
        //this.name = name;
        super(name, id);
        this.webAddress = webAddress;
        this.items = items;
    }

    public Store(long id, String name, String webAddress){
        this.id = id;
        this.name = name;
        this.webAddress = webAddress;
    }

    public Store() {
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

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
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
        Store store = (Store) o;
        return id == store.id && Objects.equals(name, store.name) && Objects.equals(webAddress, store.webAddress) && Objects.equals(items, store.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, webAddress, items, id);
    }
}
