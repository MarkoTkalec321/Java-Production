package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * Takes discount amount parameter.
 */
public record Discount(BigDecimal discountAmount) implements Serializable {

    /**
     * Takes discount amount parameter.
     *
     * @param discountAmount Type of BigDecimal.
     */
    public Discount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Optional<Object> checked = Optional.ofNullable(o);
        if (checked.isEmpty() || getClass() != o.getClass()) return false;
        Discount discount = (Discount) o;
        return Objects.equals(discountAmount, discount.discountAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountAmount);
    }
}
