package com.philvigus.dbentityfactories.attributes;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class DefaultAttribute<T> {
    private final String name;
    private final Supplier<T> valueSupplier;

    private final boolean isUnique;

    public DefaultAttribute(String name, Supplier<T> valueSupplier, boolean isUnique) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Default attribute name must not be empty");
        }

        this.name = name;
        this.valueSupplier = valueSupplier;
        this.isUnique = isUnique;
    }

    public DefaultAttribute(String name, Supplier<T> valueSupplier) {
        this(name, valueSupplier, false);
    }

    public T getValue() {
        return valueSupplier.get();
    }
}
