package com.philvigus.dbentityfactories.attributes;

import java.util.function.Supplier;

public abstract class AbstractBaseAttribute<T> {
    private final String name;
    private final Supplier<T> valueSupplier;

    protected AbstractBaseAttribute(String name, Supplier<T> valueSupplier) {
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
