package com.philvigus.dbentityfactories.testfixtures.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

/**
 * The entity with unique attributes class used by tests for this library.
 */
@Entity
@Getter
@Setter
public class EntityWithUniqueAttributes {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String uniqueString;

    @Column(unique = true)
    private Long uniqueLong;

    private String repeatableString;

    private Long repeatableLong;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final EntityWithUniqueAttributes that = (EntityWithUniqueAttributes) o;

        return Objects.equals(that.getId(), this.getId())
                && Objects.equals(this.getUniqueString(), that.getUniqueString())
                && Objects.equals(this.getUniqueLong(), that.getUniqueLong())
                && Objects.equals(this.getRepeatableLong(), that.getRepeatableLong())
                && Objects.equals(this.getRepeatableString(), that.getRepeatableString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uniqueString, uniqueLong, repeatableString, repeatableLong);
    }
}
