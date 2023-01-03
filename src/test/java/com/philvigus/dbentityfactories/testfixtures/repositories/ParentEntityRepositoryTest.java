package com.philvigus.dbentityfactories.testfixtures.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.ParentEntity;
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
class ParentEntityRepositoryTest {
    @Autowired
    ParentEntityRepository parentEntityRepository;

    @Test
    void saveShouldSaveTheEntityToTheDatabase() {
        final ParentEntity parentEntity = new ParentEntity();

        parentEntityRepository.save(parentEntity);

        final List<ParentEntity> savedEntities = parentEntityRepository.findAll();

        assertEquals(1, savedEntities.size());
        assertTrue(savedEntities.contains(parentEntity));
    }
}
