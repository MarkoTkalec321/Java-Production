package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Takes in name variable.
 */
public abstract class NamedEntity implements Serializable {

    private String name;
    private Long id;

    public NamedEntity(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NamedEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Optional<Object> checked = Optional.ofNullable(o);
        if (checked.isEmpty() || getClass() != o.getClass()) return false;
        NamedEntity that = (NamedEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
