package com.philvigus.dbentityfactories.attributes;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class DefaultAttribute<T> extends AbstractBaseAttribute<T> {
    private final boolean isUnique;

    public DefaultAttribute(String name, Supplier<T> valueSupplier, boolean isUnique) {
        super(name, valueSupplier);

        this.isUnique = isUnique;
    }

    public DefaultAttribute(String name, Supplier<T> valueSupplier) {
        this(name, valueSupplier, false);
    }

    public boolean isUnique() {
        return isUnique;
    }
}
