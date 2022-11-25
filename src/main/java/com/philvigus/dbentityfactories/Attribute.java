package com.philvigus.dbentityfactories;

import lombok.Getter;

@Getter
public class Attribute<T> {
    private final String name;
    private final T value;
    private final boolean isUnique;

    public Attribute(String name, T value) {
        this.name = name;
        this.value = value;
        this.isUnique = false;
    }

    public Attribute(String name, T value, boolean isUnique) {
        this.name = name;
        this.value = value;
        this.isUnique = isUnique;
    }
}
