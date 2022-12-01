package com.philvigus.dbentityfactories;

import lombok.Getter;

@Getter
public class Attribute<T> {
    private final String name;
    private final T value;
    private final boolean isUnique;

    public Attribute(final String name, final T value) {
        this(name, value, false);
    }

    public Attribute(final String name, final T value, final boolean isUnique) {
        this.name = name;
        this.value = value;
        this.isUnique = isUnique;
    }
}
