package com.philvigus.dbentityfactories.testfixtures.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.ChildEntity;
import com.philvigus.dbentityfactories.testfixtures.entities.ParentEntity;
import com.philvigus.dbentityfactories.testfixtures.hibernate.repositories.ChildEntityRepository;
import com.philvigus.dbentityfactories.testfixtures.hibernate.repositories.ParentEntityRepository;
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

    @Autowired
    ChildEntityRepository childEntityRepository;

    @Test
    void saveShouldSaveTheEntityToTheDatabase() {
        final ParentEntity parentEntity = new ParentEntity();

        parentEntityRepository.save(parentEntity);

        final List<ParentEntity> savedEntities = parentEntityRepository.findAll();

        assertEquals(1, savedEntities.size());
        assertTrue(savedEntities.contains(parentEntity));
    }

    @Test
    void saveShouldSaveTheParentEntitysChildrenIfItHasThem() {
        final ParentEntity parentEntity = new ParentEntity();

        final ChildEntity firstChild = new ChildEntity();
        final ChildEntity secondChild = new ChildEntity();

        parentEntity.addChild(firstChild);
        parentEntity.addChild(secondChild);

        parentEntityRepository.save(parentEntity);

        final List<ChildEntity> savedChildEntities = childEntityRepository.findAll();

        assertEquals(2, savedChildEntities.size());
        assertTrue(savedChildEntities.contains(firstChild));
        assertTrue(savedChildEntities.contains(secondChild));
    }
}
