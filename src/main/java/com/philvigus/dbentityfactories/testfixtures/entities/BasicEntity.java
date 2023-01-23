package com.philvigus.dbentityfactories.testfixtures.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * The Basic entity class used by tests for this library
 */
@Entity
@Getter
@Setter
public class BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long myLongAttribute;

    private String myStringAttribute;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
                return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BasicEntity that = (BasicEntity) o;

        return Objects.equals(that.getId(), this.getId())
                && Objects.equals(that.getMyLongAttribute(), this.getMyLongAttribute())
                && Objects.equals(that.getMyStringAttribute(), this.getMyStringAttribute());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, myLongAttribute, myStringAttribute);
    }
}
