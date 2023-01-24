package com.philvigus.dbentityfactories.attributes;

import lombok.Getter;

import java.util.function.Supplier;


/**
 * The Custom attribute class.
 * <p>
 * Used when a user wants to override one of the default attributes on an entity factory
 *
 * @param <T> the type of the attribute
 */
@Getter
public class CustomAttribute<T> extends BaseAttribute<T> {
    /**
     * Instantiates a new Custom attribute.
     *
     * @param name          the attribute name
     * @param valueSupplier the value supplier used to generate the attribute's value
     */
    public CustomAttribute(final String name, final Supplier<T> valueSupplier) {
        super(name, valueSupplier);
    }
}
