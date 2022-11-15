package com.philvigus.dbentityfactories.testfixtures.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicEntityTest {
    BasicEntity basicEntity;

    @BeforeEach
    void setUp() {
        basicEntity = new BasicEntity();
    }

    @Test
    void setMyLongAttributeShouldSetMyLongAttribute() {
        final Long attributeValue = 1L;

        basicEntity.setMyLongAttribute(attributeValue);

        assertEquals(attributeValue, basicEntity.getMyLongAttribute());
    }

    @Test
    void setMyStringAttributeShouldSetMyStringAttribute() {
        final String attributeValue = "Test Value";

        basicEntity.setMyStringAttribute(attributeValue);

        assertEquals(attributeValue, basicEntity.getMyStringAttribute());
    }
}
