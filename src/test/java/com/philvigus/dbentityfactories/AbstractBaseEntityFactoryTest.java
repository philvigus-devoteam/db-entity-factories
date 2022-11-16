package com.philvigus.dbentityfactories;

import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import com.philvigus.dbentityfactories.testfixtures.factories.BasicEntityFactory;
import com.philvigus.dbentityfactories.testfixtures.repositories.BasicEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class AbstractBaseEntityFactoryTest {
    BasicEntityFactory basicEntityFactory;

    @Autowired
    BasicEntityRepository basicEntityRepository;

    @BeforeEach
    void setUp() {
        basicEntityFactory = new BasicEntityFactory(basicEntityRepository);
    }

    @Test
    void makeCanReturnASingleBasicEntityWithItsAttributesCorrectlySet() {
        BasicEntity basicEntity = basicEntityFactory.make();

        assertBasicEntityCorrectlyMade(basicEntity);
    }

    @Test
    void makeCanReturnAListOfMultipleBasicEntitiesWithTheirAttributesCorrectlySet() {
        final int numberOfEntities = 2;

        List<BasicEntity> basicEntities = basicEntityFactory.make(numberOfEntities);

        assertEquals(numberOfEntities, basicEntities.size());
        assertBasicEntityCorrectlyMade(basicEntities.get(0));
        assertBasicEntityCorrectlyMade(basicEntities.get(1));
    }

    @Test
    void makeThrowsAnExceptionIfCopiesIsLessThanOne() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> basicEntityFactory.make(0));
    }

    @Test
    void createCanReturnAndSaveASingleBasicEntityWithItsAttributesCorrectlySetToTheDatabase() {
        BasicEntity basicEntity = basicEntityFactory.create();
        List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertBasicEntityCorrectlyCreated(basicEntity);
        
        assertEquals(1, savedEntities.size());
        assertTrue(savedEntities.contains(basicEntity));
    }

    @Test
    void createCanReturnAndSaveMultipleBasicEntitiesWithTheirAttributesCorrectlySetToTheDatabase() {
        List<BasicEntity> basicEntities = basicEntityFactory.create(2);
        List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertEquals(2, basicEntities.size());
        assertBasicEntityCorrectlyCreated(basicEntities.get(0));
        assertBasicEntityCorrectlyCreated(basicEntities.get(1));

        assertEquals(2, savedEntities.size());
        assertTrue(savedEntities.contains(basicEntities.get(0)));
        assertTrue(savedEntities.contains(basicEntities.get(1)));
    }

    @Test
    void createThrowsAnExceptionIfCopiesIsLessThanOne() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> basicEntityFactory.create(0));
    }

    void assertBasicEntityCorrectlyMade(final BasicEntity basicEntity) {
        assertTrue(basicEntity instanceof BasicEntity);
        assertNull(basicEntity.getId());
        assertTrue(basicEntity.getMyLongAttribute() instanceof Long);
        assertTrue(basicEntity.getMyStringAttribute() instanceof String);
    }

    void assertBasicEntityCorrectlyCreated(final BasicEntity basicEntity) {
        assertTrue(basicEntity instanceof BasicEntity);
        assertTrue(basicEntity.getId() instanceof Long);
        assertTrue(basicEntity.getMyLongAttribute() instanceof Long);
        assertTrue(basicEntity.getMyStringAttribute() instanceof String);
    }
}
