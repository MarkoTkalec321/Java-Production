package hr.java.production.model;

import java.math.BigDecimal;

/**
 * Has calculateKilocalories and calculatePrice methods.
 */
public interface Edible {

    Integer calculateKilocalories();

    BigDecimal calculatePrice();
}
