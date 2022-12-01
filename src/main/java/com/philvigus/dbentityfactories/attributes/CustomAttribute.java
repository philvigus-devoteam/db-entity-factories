package com.philvigus.dbentityfactories.attributes;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class CustomAttribute<T> extends AbstractBaseAttribute<T> {
    public CustomAttribute(final String name, final Supplier<T> valueSupplier) {
        super(name, valueSupplier);
    }
}
