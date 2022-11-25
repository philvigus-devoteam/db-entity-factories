package com.philvigus.dbentityfactories;

import lombok.Getter;

@Getter
public class Attribute {
    private final String name;
    private final Object value;
    private final boolean isUnique;

    public Attribute(String name, Object value) {
        this.name = name;
        this.value = value;
        this.isUnique = false;
    }

    public Attribute(String name, Object value, boolean isUnique) {
        this.name = name;
        this.value = value;
        this.isUnique = isUnique;
    }
}
