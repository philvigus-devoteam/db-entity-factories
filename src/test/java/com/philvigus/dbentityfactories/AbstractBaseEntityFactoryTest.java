package com.philvigus.dbentityfactories;

import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import com.philvigus.dbentityfactories.testfixtures.factories.BasicEntityFactory;
import com.philvigus.dbentityfactories.testfixtures.repositories.BasicEntityRepository;
import org.junit.jupiter.api.Assertions;
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
    @Autowired
    BasicEntityRepository basicEntityRepository;

    @Test
    void makeCanReturnASingleBasicEntityWithItsAttributesCorrectlySet() {
        BasicEntityFactory basicEntityFactory = new BasicEntityFactory(basicEntityRepository);
        BasicEntity basicEntity = basicEntityFactory.make();

        assertTrue(basicEntity instanceof BasicEntity);
        assertNull(basicEntity.getId());
        assertTrue(basicEntity.getMyLongAttribute() instanceof Long);
        assertTrue(basicEntity.getMyStringAttribute() instanceof String);

    }

    @Test
    void makeCanReturnAListOfMultipleBasicEntitiesWithTheirAttributesCorrectlySet() {
        BasicEntityFactory basicEntityFactory = new BasicEntityFactory(basicEntityRepository);
        List<BasicEntity> basicEntities = basicEntityFactory.make(2);

        assertEquals(2, basicEntities.size());

        assertTrue(basicEntities.get(0) instanceof BasicEntity);
        assertNull(basicEntities.get(0).getId());
        assertTrue(basicEntities.get(0).getMyLongAttribute() instanceof Long);
        assertTrue(basicEntities.get(0).getMyStringAttribute() instanceof String);

        assertTrue(basicEntities.get(1) instanceof BasicEntity);
        assertNull(basicEntities.get(1).getId());
        assertTrue(basicEntities.get(1).getMyLongAttribute() instanceof Long);
        assertTrue(basicEntities.get(1).getMyStringAttribute() instanceof String);
    }

    @Test
    void makeThrowsAnExceptionIfCopiesIsLessThanOne() {
        BasicEntityFactory basicEntityFactory = new BasicEntityFactory(basicEntityRepository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> basicEntityFactory.make(0));
    }

    @Test
    void createCanReturnAndSaveASingleBasicEntityWithItsAttributesCorrectlySetToTheDatabase() {
        BasicEntityFactory basicEntityFactory = new BasicEntityFactory(basicEntityRepository);
        BasicEntity basicEntity = basicEntityFactory.create();

        assertTrue(basicEntity instanceof BasicEntity);
        assertTrue(basicEntity.getId() instanceof Long);
        assertTrue(basicEntity.getMyLongAttribute() instanceof Long);
        assertTrue(basicEntity.getMyStringAttribute() instanceof String);

        List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertEquals(1, savedEntities.size());
        assertTrue(savedEntities.contains(basicEntity));
    }

    @Test
    void createCanReturnAndSaveMultipleBasicEntitiesWithTheirAttributesCorrectlySetToTheDatabase() {
        BasicEntityFactory basicEntityFactory = new BasicEntityFactory(basicEntityRepository);
        List<BasicEntity> basicEntities = basicEntityFactory.create(2);

        assertEquals(2, basicEntities.size());

        assertTrue(basicEntities.get(0) instanceof BasicEntity);
        assertTrue(basicEntities.get(0).getId() instanceof Long);
        assertTrue(basicEntities.get(0).getMyLongAttribute() instanceof Long);
        assertTrue(basicEntities.get(0).getMyStringAttribute() instanceof String);

        assertTrue(basicEntities.get(1) instanceof BasicEntity);
        assertTrue(basicEntities.get(1).getId() instanceof Long);
        assertTrue(basicEntities.get(1).getMyLongAttribute() instanceof Long);
        assertTrue(basicEntities.get(1).getMyStringAttribute() instanceof String);

        List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertEquals(2, savedEntities.size());
        assertTrue(savedEntities.contains(basicEntities.get(0)));
        assertTrue(savedEntities.contains(basicEntities.get(1)));
    }

    @Test
    void createThrowsAnExceptionIfCopiesIsLessThanOne() {
        BasicEntityFactory basicEntityFactory = new BasicEntityFactory(basicEntityRepository);

        Assertions.assertThrows(IllegalArgumentException.class, () -> basicEntityFactory.create(0));
    }
}
