package com.philvigus.dbentityfactories;

import lombok.Getter;

/**
 * The Attribute class.
 *
 * @param <T> the type of the attributes value
 */
@Getter
public class Attribute<T> {
    private final String name;
    private final T value;
    private final boolean isUnique;

    /**
     * Instantiates a new Attribute.
     *
     * @param name  the attribute name
     * @param value the attribute value
     */
    public Attribute(final String name, final T value) {
        this(name, value, false);
    }

    /**
     * Instantiates a new Attribute.
     *
     * @param name     the attribute name
     * @param value    the attribute value
     * @param isUnique whether the attribute value is unique
     */
    public Attribute(final String name, final T value, final boolean isUnique) {
        this.name = name;
        this.value = value;
        this.isUnique = isUnique;
    }
}
