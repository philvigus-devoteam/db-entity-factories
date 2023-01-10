package com.philvigus.dbentityfactories.attributes;

import com.philvigus.dbentityfactories.exceptions.EntityFactoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DefaultAttributeTest {
    final Integer defaultAttributeValue = 5;
    final  DefaultAttribute<Integer> defaultAttribute = new DefaultAttribute<>("defaultIntegerAttribute", () -> defaultAttributeValue);
    @RepeatedTest(20)
    void getValueReturnsAConstantForAConstantAttribute() {
        final Integer firstValue = defaultAttribute.getValue();
        final Integer secondValue = defaultAttribute.getValue();

        assertEquals(defaultAttributeValue, firstValue);
        assertEquals(defaultAttributeValue, secondValue);
    }

    @RepeatedTest(20)
    void getValueReturnsDifferentValuesWhenUsedMultipleTimesForANonConstantAttribute() {
        final DefaultAttribute<UUID> defaultAttribute = new DefaultAttribute<>("defaultIntegerAttribute", UUID::randomUUID);

        final UUID firstValue = defaultAttribute.getValue();
        final UUID secondValue = defaultAttribute.getValue();

        assertNotEquals(firstValue, secondValue);
    }

    @Test
    void getValueThrowsAnExceptionIfTheAttributeIsUniqueAndNoUniqueValuesCanBeFound() {
        final DefaultAttribute<Integer> defaultAttribute = new DefaultAttribute<>("defaultIntegerAttribute", () -> 5, true);

        defaultAttribute.getValue();

        Assertions.assertThrows(EntityFactoryException.class, defaultAttribute::getValue);
    }
}
