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

    public DefaultAttribute(String name, Supplier<T> valueSupplier, boolean isUnique) {
        super(name, valueSupplier);

        this.isUnique = isUnique;
        this.usedValues = new HashSet<>();
    }

    public DefaultAttribute(String name, Supplier<T> valueSupplier) {
        this(name, valueSupplier, false);
    }

    public boolean hasUsedValue(Object value) {
        try {
            return usedValues.contains(value);
        } catch (ClassCastException e) {
            throw new EntityFactoryException("Unable to check whether value is used as it is of the wrong type", e);
        }
    }

    public void addUsedValue(Object value) {
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
