package com.philvigus.dbentityfactories.attributes;

import com.philvigus.dbentityfactories.exceptions.EntityFactoryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DefaultAttributeTest {
    @Test
    void getValueReturnsAConstantForAConstantAttribute() {
        final DefaultAttribute<Integer> defaultAttribute = new DefaultAttribute<>("defaultIntegerAttribute", () -> 5);

        assertEquals(5, defaultAttribute.getValue());
        assertEquals(5, defaultAttribute.getValue());
    }

    @Test
    void getValueReturnsAVariableForANonConstantAttribute() throws InterruptedException {
        final DefaultAttribute<Long> defaultAttribute = new DefaultAttribute<>("defaultIntegerAttribute", System::currentTimeMillis);

        Long firstValue = defaultAttribute.getValue();

        // TODO: replace this with something less brittle
        Thread.sleep(100);

        Long secondValue = defaultAttribute.getValue();

        assertNotEquals(firstValue, secondValue);

    }

    @Test
    void getValueThrowsAnExceptionIfTheAttributeIsUniqueAndNoUniqueValuesCanBeFound() {
        final DefaultAttribute<Integer> defaultAttribute = new DefaultAttribute<>("defaultIntegerAttribute", () -> 5, true);

        defaultAttribute.getValue();

        Assertions.assertThrows(EntityFactoryException.class, defaultAttribute::getValue);
    }

    @Test
    void getValueReturnsUniqueAttributeIsUnique() {
        final DefaultAttribute<Integer> defaultAttribute = new DefaultAttribute<>("defaultIntegerAttribute", () -> 5, true);

        defaultAttribute.getValue();

        Assertions.assertThrows(EntityFactoryException.class, defaultAttribute::getValue);
    }
}
