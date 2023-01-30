package hr.java.production.genericsi;

import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.model.Technical;

import java.util.List;
import java.util.Set;

public class TechnicalStore<T extends Technical> extends Store {
    private List<T> technicalItems;

    /**
     * Takes in name, webAddress and items.
     *
     * @param name       Type String.
     * @param webAddress Type String.
     * @param items      Type Item.
     */
    public TechnicalStore(String name, long id, String webAddress, Set<Item> items) {
        super(name, id, webAddress, items);

    }

    public List<T> getTechnicalItems() {
        return technicalItems;
    }

    public void setTechnicalItems(List<T> technicalItems) {
        this.technicalItems = technicalItems;
    }
}
