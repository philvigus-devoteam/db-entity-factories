package com.philvigus.dbentityfactories.attributes;

import lombok.Getter;

import java.util.function.Supplier;

@Getter
public class CustomAttribute<T> extends AbstractBaseAttribute<T> {
    public CustomAttribute(String name, Supplier<T> valueSupplier) {
        super(name, valueSupplier);
    }
}
