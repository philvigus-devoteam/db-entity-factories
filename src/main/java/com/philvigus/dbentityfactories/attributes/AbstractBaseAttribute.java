package com.philvigus.dbentityfactories.attributes;

import java.util.function.Supplier;

public abstract class AbstractBaseAttribute<T> {
    protected final Supplier<T> valueSupplier;
    private final String name;

    protected AbstractBaseAttribute(final String name, final Supplier<T> valueSupplier) {
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
