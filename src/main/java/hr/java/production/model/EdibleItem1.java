package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Takes in parameters of item that is edible and implements Edible interface.
 */
public class EdibleItem1 extends Item implements Edible, Serializable {

    private static final Integer NUMBER_OF_CALORIES_PER_KILO = 600;
    BigDecimal weight = new BigDecimal("2.5");

    /**
     * Takes parameters from type of Item.
     *
     * @param name           Type string.
     * @param category       Type Category.
     * @param width          Type BigDecimal.
     * @param height         Type BigDecimal.
     * @param length         Type BigDecimal.
     * @param productionCost Type BigDecimal.
     * @param sellingPrice   Type BigDecimal.
     * @param discountAmount Type Discount.
     */
    public EdibleItem1(String name, long id, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                       BigDecimal productionCost, BigDecimal sellingPrice, Discount discountAmount) {
        super(name, id, category, width, height, length, productionCost, sellingPrice, discountAmount);
    }

    /**
     * Needed for calling the class.
     */
    public EdibleItem1() {
    }

    /**
     * Overrides calculateKilocalories from interface Edible.
     *
     * @return Returns Kilocalories type Integer.
     */
    @Override
    public Integer calculateKilocalories() {

        Integer Kilocalories = NUMBER_OF_CALORIES_PER_KILO * weight.intValue();//zaokruziti ?

        return Kilocalories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Optional<Object> checked = Optional.ofNullable(o);
        if (checked.isEmpty() || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EdibleItem1 that = (EdibleItem1) o;
        return Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), weight);
    }

    /**
     * Overrides calculatePrice from interface Edible.
     *
     * @return Returns calculated price type BigDecimal.
     */
    @Override
    public BigDecimal calculatePrice() {

        BigDecimal hundred = new BigDecimal("0.01");

        BigDecimal pricePerKilo = getSellingPrice().multiply(weight);
        BigDecimal discount = getDiscountAmount().discountAmount().multiply(hundred);
        BigDecimal sub = pricePerKilo.multiply(discount);
        BigDecimal price = pricePerKilo.subtract(sub);

        return price;

    }
}
