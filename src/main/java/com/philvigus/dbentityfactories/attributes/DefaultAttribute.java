package com.philvigus.dbentityfactories.attributes;

import com.philvigus.dbentityfactories.exceptions.EntityFactoryException;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * The Default attribute class.
 * <p>
 * Used as part of defining entity factories to specify how their attributes should be generated.
 *
 * @param <T> the type of the attribute
 */
@Getter
public class DefaultAttribute<T> extends BaseAttribute<T> {
    /**
     * Defines the maximum attempts to generate a unique value before an exception is thrown.
     */
    public static final int MAX_UNIQUE_ATTEMPTS = 100;

    private final boolean isUnique;

    private final Set<T> usedValues;

    /**
     * Instantiates a new Default attribute.
     *
     * @param name          the attribute name
     * @param valueSupplier the value supplier used to generate the attribute value
     * @param isUnique      sets whether the attribute values have to be unique
     */
    public DefaultAttribute(final String name, final Supplier<T> valueSupplier, final boolean isUnique) {
        super(name, valueSupplier);

        this.isUnique = isUnique;
        this.usedValues = new HashSet<>();
    }

    /**
     * Instantiates a new Default attribute with isUnique defaulting to false.
     *
     * @param name          the attribute name
     * @param valueSupplier the value supplier used to generate the attribute value
     */
    public DefaultAttribute(final String name, final Supplier<T> valueSupplier) {
        this(name, valueSupplier, false);
    }

    /**
     * Returns whether a value has already been used for this attribute.
     *
     * @param value the value to check
     * @return whether the value has been used already
     */
    public boolean hasUsedValue(final Object value) {
        try {
            return usedValues.contains((T) value);
        } catch (ClassCastException e) {
            throw new EntityFactoryException("Unable to check whether value is used as it is of the wrong type", e);
        }
    }

    /**
     * Adds a value to the used value list.
     *
     * @param value the value to be added
     */
    public void addUsedValue(final Object value) {
        try {
            usedValues.add((T) value);
        } catch (ClassCastException e) {
            throw new EntityFactoryException("Unable to add value as it is of the wrong type", e);
        }
    }

    /**
     * Sets whether the attribute values generated have to be unique.
     *
     * @return the boolean
     */
    public boolean isUnique() {
        return isUnique;
    }

    @Override
    public T getValue() {
        return isUnique() ? getUniqueValue() : valueSupplier.get();
    }

    /**
     * Clears the list of values used for this entity.
     */
    public void clearUsedValues() {
        usedValues.clear();
    }

    private T getUniqueValue() {
        T value;
        int attempts = 0;

        do {
            // bail out and throw an error after a set number of attempts to find a unique value
            if (attempts == DefaultAttribute.MAX_UNIQUE_ATTEMPTS) {
                throw new EntityFactoryException(String.format(
                        "Unable to find unique value for attribute %s after %s attempts",
                        getName(),
                        DefaultAttribute.MAX_UNIQUE_ATTEMPTS)
                );
            }

            value = valueSupplier.get();
            attempts++;
        } while (usedValues.contains(value));

        usedValues.add(value);

        return value;
    }
}
