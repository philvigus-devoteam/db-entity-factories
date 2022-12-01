package com.philvigus.dbentityfactories.testfixtures.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.EntityWithUniqueAttributes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class EntityWithUniqueAttributesRepositoryTest {
    @Autowired
    EntityWithUniqueAttributesRepository entityWithUniqueAttributesRepository;

    @Test
    void saveShouldSaveTheEntityToTheDatabase() {
        final EntityWithUniqueAttributes entityWithUniqueAttributes = new EntityWithUniqueAttributes();

        entityWithUniqueAttributes.setUniqueString("string attrib");
        entityWithUniqueAttributes.setUniqueLong(1L);
        entityWithUniqueAttributes.setRepeatableString("string attrib");
        entityWithUniqueAttributes.setRepeatableLong(1L);

        entityWithUniqueAttributesRepository.save(entityWithUniqueAttributes);

        final List<EntityWithUniqueAttributes> savedEntities = entityWithUniqueAttributesRepository.findAll();

        assertEquals(1, savedEntities.size());
        assertTrue(savedEntities.contains(entityWithUniqueAttributes));
    }
}
