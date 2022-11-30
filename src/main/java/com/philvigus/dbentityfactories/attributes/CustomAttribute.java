package com.philvigus.dbentityfactories.attributes;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class CustomAttribute<T> implements Attribute<T> {
    private final String name;
    private final Supplier<T> valueSupplier;

    public CustomAttribute(String name, Supplier<T> valueSupplier) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Default attribute name must not be empty");
        }

        this.name = name;
        this.valueSupplier = valueSupplier;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return valueSupplier.get();
    }
}
