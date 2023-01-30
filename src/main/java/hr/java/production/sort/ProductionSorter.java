package hr.java.production.sort;

import hr.java.production.model.Item;

import java.util.Comparator;

/**
 * Overrides compare function to compare 2 prices.
 */

public class ProductionSorter implements Comparator<Item> {

    @Override
    public int compare(Item i1, Item i2) {

        if (i1.getSellingPrice().compareTo(i2.getSellingPrice()) > 0) {
            return 1;
        } else if (i1.getSellingPrice().compareTo(i2.getSellingPrice()) < 0) {
            return -1;
        } else {
            return i1.getName().compareTo(i2.getName());
        }

    }
}
