package hr.java.production.model;

/**
 * Has getWarrany method.
 */
public sealed interface Technical permits Laptop {

    Integer getWarranty();
}
