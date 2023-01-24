package com.philvigus.dbentityfactories.attributes;

import java.util.function.Supplier;

/**
 * The base attribute class. Cannot be instantiated.
 *
 * @param <T> The type of the attribute
 */
public abstract class BaseAttribute<T> {

    /**
     * Called whenever an attribute is used as part of a factory to generate the attribute's value.
     */
    protected final Supplier<T> valueSupplier;

    /**
     * The attribute name.
     */
    protected final String name;

    /**
     * Instantiates a new base attribute.
     *
     * @param name          the attribute name
     * @param valueSupplier the value supplier used to generate the value of the attribute
     */
    protected BaseAttribute(final String name, final Supplier<T> valueSupplier) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Default attribute name must not be empty");
        }

        this.name = name;
        this.valueSupplier = valueSupplier;
    }

    /**
     * Gets the attribute name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the attribute value by calling the value supplier.
     *
     * @return the value
     */
    public T getValue() {
        return valueSupplier.get();
    }
}
