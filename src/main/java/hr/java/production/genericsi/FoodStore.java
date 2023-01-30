package hr.java.production.genericsi;

import hr.java.production.model.Edible;
import hr.java.production.model.Item;
import hr.java.production.model.Store;

import java.util.List;
import java.util.Set;

public class FoodStore<T extends Edible> extends Store {
    private List<T> foodItems;

    /**
     * Takes in name, webAddress and items.
     *
     * @param name       Type String.
     * @param webAddress Type String.
     * @param items      Type Item.
     */
    public FoodStore(String name, long id, String webAddress, Set<Item> items) {
        super(name, id, webAddress, items);
    }

    public List<T> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<T> foodItems) {
        this.foodItems = foodItems;
    }
}
