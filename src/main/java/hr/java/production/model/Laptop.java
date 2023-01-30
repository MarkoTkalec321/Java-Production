package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Takes in parameters from item and sets and gets warranty.
 */
public final class Laptop extends Item implements Technical, Serializable {

    Integer warranty;

    /**
     * Takes in name, category, width, height, length, productionCost, sellingPrice, discountAmount.
     *
     * @param name           Type String.
     * @param category       Type Category.
     * @param width          Type BigDecimal.
     * @param height         Type BigDecimal.
     * @param length         Type BigDecimal.
     * @param productionCost Type BigDecimal.
     * @param sellingPrice   Type BigDecimal.
     * @param discountAmount Type Discount.
     */
    public Laptop(String name, long id, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                  BigDecimal productionCost, BigDecimal sellingPrice, Discount discountAmount) {
        super(name, id, category, width, height, length, productionCost, sellingPrice, discountAmount);
    }


    /**
     * Overrides method getWarranty from sealed interface Technical and returns warranty type Integer.
     *
     * @return
     */
    @Override
    public Integer getWarranty() {
        return warranty;
    }

    public void setWarranty(Integer warranty) {
        this.warranty = warranty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Optional<Object> checked = Optional.ofNullable(o);
        if (checked.isEmpty() || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Laptop laptop = (Laptop) o;
        return Objects.equals(warranty, laptop.warranty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), warranty);
    }
}
