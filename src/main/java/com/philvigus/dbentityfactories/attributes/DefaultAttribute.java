package com.philvigus.dbentityfactories.attributes;

import com.philvigus.dbentityfactories.exceptions.EntityFactoryException;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Getter
public class DefaultAttribute<T> extends AbstractBaseAttribute<T> {
    public static final int MAX_UNIQUE_ATTEMPTS = 100;

    private final boolean isUnique;

    private final Set<T> usedValues;

    public DefaultAttribute(final String name, final Supplier<T> valueSupplier, final boolean isUnique) {
        super(name, valueSupplier);

        this.isUnique = isUnique;
        this.usedValues = new HashSet<>();
    }

    public DefaultAttribute(final String name, final Supplier<T> valueSupplier) {
        this(name, valueSupplier, false);
    }

    public boolean hasUsedValue(final Object value) {
        try {
            return usedValues.contains((T) value);
        } catch (ClassCastException e) {
            throw new EntityFactoryException("Unable to check whether value is used as it is of the wrong type", e);
        }
    }

    public void addUsedValue(final Object value) {
        try {
            usedValues.add((T) value);
        } catch (ClassCastException e) {
            throw new EntityFactoryException("Unable to add value as it is of the wrong type", e);
        }
    }

    public boolean isUnique() {
        return isUnique;
    }

    @Override
    public T getValue() {
        return isUnique() ? getUniqueValue() : valueSupplier.get();
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
