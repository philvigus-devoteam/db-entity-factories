package com.philvigus.dbentityfactories.testfixtures.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
public class ChildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_id")
    private ParentEntity parent;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChildEntity that = (ChildEntity) o;

        return Objects.equals(id, that.id) && Objects.equals(this.getParent(), that.getParent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parent);
    }
}
