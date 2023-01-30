package hr.java.production.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Takes in width, height, length, productionCost, sellingPrice, category, discountAmount from items.
 */
public class Item extends NamedEntity implements Serializable {

    private String name;
    private BigDecimal width, height, length, productionCost, sellingPrice;
    private Category category;
    private Discount discountAmount;
    private long id;
    private long categoryId;

    /**
     * Takes in width, height, length, productionCost, sellingPrice, category, discountAmount from items.
     */
    public Item(String name, long id, Category category, BigDecimal width, BigDecimal height, BigDecimal length,
                BigDecimal productionCost, BigDecimal sellingPrice, Discount discountAmount) {
        super(name, id);
        this.category = category;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
        this.discountAmount = discountAmount;
    }

    public Item(long id, long categoryId, String name, BigDecimal width, BigDecimal height,
                BigDecimal length, BigDecimal productionCost, BigDecimal sellingPrice) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;
        this.productionCost = productionCost;
        this.sellingPrice = sellingPrice;
    }

    /**
     * Needed to call a class.
     */
    public Item() {}



    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getProductionCost() {
        return productionCost;
    }

    public void setProductionCost(BigDecimal productionCost) {
        this.productionCost = productionCost;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Discount getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Discount discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDimensions() {
        return height.multiply(width).multiply(length);
    }

    @Override
    public String toString() {
        return name + getClass();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Item item = (Item) o;
        return id == item.id && Objects.equals(name, item.name) && Objects.equals(width, item.width) && Objects.equals(height, item.height) && Objects.equals(length, item.length) && Objects.equals(productionCost, item.productionCost) && Objects.equals(sellingPrice, item.sellingPrice) && Objects.equals(category, item.category) && Objects.equals(discountAmount, item.discountAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, width, height, length, productionCost, sellingPrice, category, discountAmount, id);
    }
}
