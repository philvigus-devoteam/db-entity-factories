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
class ChildEntityRepositoryTest {
    @Autowired
    ChildEntityRepository childEntityRepository;

    @Autowired
    ParentEntityRepository parentEntityRepository;

    @Test
    void saveShouldSaveTheEntityToTheDatabase() {
        final ParentEntity parentEntity = new ParentEntity();
        final ChildEntity childEntity = new ChildEntity();

        parentEntityRepository.save(parentEntity);

        parentEntity.addChild(childEntity);

        childEntityRepository.save(childEntity);

        final List<ChildEntity> savedChildEntities = childEntityRepository.findAll();
        assertEquals(1, savedChildEntities.size());
        assertTrue(savedChildEntities.contains(childEntity));
    }
}
