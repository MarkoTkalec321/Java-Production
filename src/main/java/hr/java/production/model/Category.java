package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Sets and gets name and description parameters.
 */
public class Category extends NamedEntity implements Serializable {

    private String name;
    private String description;
    private long id;


    /**
     * Takes a name and description parameters.
     *
     * @param name        Type String.
     * @param description Type String.
     */
    public Category(String name, long id, String description) {
        super(name, id);
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name) && Objects.equals(description, category.description) && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, id);
    }

    /*@Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }*/


}

