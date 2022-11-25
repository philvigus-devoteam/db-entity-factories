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
import java.util.Map;

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
        final BasicEntity basicEntity = basicEntityFactory.make();

        assertBasicEntityCorrectlyMadeWithDefaultAttributes(basicEntity);
    }

    @Test
    void makeCanReturnAListOfMultipleBasicEntitiesWithTheirAttributesCorrectlySet() {
        final int numberOfEntities = 2;

        final List<BasicEntity> basicEntities = basicEntityFactory.make(numberOfEntities);

        assertEquals(numberOfEntities, basicEntities.size());
        assertBasicEntityCorrectlyMadeWithDefaultAttributes(basicEntities.get(0));
        assertBasicEntityCorrectlyMadeWithDefaultAttributes(basicEntities.get(1));
    }

    @Test
    void makeCanReturnASingleBasicEntityWithCustomAttributesSet() {
        final String customLongName = "myLongAttribute";
        final Long customLongValue = 999L;

        final String customStringName = "myStringAttribute";
        final String customStringValue = "a custom string value";

        final BasicEntity basicEntity = basicEntityFactory.withAttributes(
                Map.of(
                        "myLongAttribute", new Attribute(customLongName, customLongValue),
                        "myStringAttribute", new Attribute(customStringName, customStringValue)
                )
        ).make();

        assertNull(basicEntity.getId());
        assertEquals(customLongValue, basicEntity.getMyLongAttribute());
        assertEquals(customStringValue, basicEntity.getMyStringAttribute());
    }

    @Test
    void makeCanReturnMultipleBasicEntitiesWithCustomAttributesSet() {
        final int numberOfEntities = 2;

        final String customLongName = "myLongAttribute";
        final Long customLongValue = 999L;

        final String customStringName = "myStringAttribute";
        final String customStringValue = "a custom string value";

        final List<BasicEntity> basicEntities = basicEntityFactory.withAttributes(
                Map.of(
                        "myLongAttribute", new Attribute(customLongName, customLongValue),
                        "myStringAttribute", new Attribute(customStringName, customStringValue)
                )
        ).make(numberOfEntities);

        assertNull(basicEntities.get(0).getId());
        assertEquals(customLongValue, basicEntities.get(0).getMyLongAttribute());
        assertEquals(customStringValue, basicEntities.get(0).getMyStringAttribute());

        assertNull(basicEntities.get(1).getId());
        assertEquals(customLongValue, basicEntities.get(1).getMyLongAttribute());
        assertEquals(customStringValue, basicEntities.get(1).getMyStringAttribute());
    }

    @Test
    void makeThrowsAnExceptionIfCopiesIsLessThanOne() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> basicEntityFactory.make(0));
    }

    @Test
    void createCanReturnAndSaveASingleBasicEntityWithItsAttributesCorrectlySetToTheDatabase() {
        final BasicEntity basicEntity = basicEntityFactory.create();
        final List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertBasicEntityCorrectlyCreatedWithDefaultAttributes(basicEntity);

        assertEquals(1, savedEntities.size());
        assertTrue(savedEntities.contains(basicEntity));
    }

    @Test
    void createCanReturnAndSaveMultipleBasicEntitiesWithTheirAttributesCorrectlySetToTheDatabase() {
        final int numberOfEntities = 2;

        final List<BasicEntity> basicEntities = basicEntityFactory.create(numberOfEntities);
        final List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertEquals(numberOfEntities, basicEntities.size());
        assertBasicEntityCorrectlyCreatedWithDefaultAttributes(basicEntities.get(0));
        assertBasicEntityCorrectlyCreatedWithDefaultAttributes(basicEntities.get(1));

        assertEquals(numberOfEntities, savedEntities.size());
        assertTrue(savedEntities.contains(basicEntities.get(0)));
        assertTrue(savedEntities.contains(basicEntities.get(1)));
    }

    @Test
    void createCanReturnAndSaveASingleBasicEntityWithCustomAttributesToTheDatabase() {
        final String customLongName = "myLongAttribute";
        final Long customLongValue = 999L;

        final String customStringName = "myStringAttribute";
        final String customStringValue = "a custom string value";

        final BasicEntity basicEntity = basicEntityFactory.withAttributes(
                Map.of(
                        "myLongAttribute", new Attribute(customLongName, customLongValue),
                        "myStringAttribute", new Attribute(customStringName, customStringValue)
                )
        ).create();

        final List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertEquals(customLongValue, basicEntity.getMyLongAttribute());
        assertEquals(customStringValue, basicEntity.getMyStringAttribute());

        assertEquals(1, savedEntities.size());
        assertTrue(savedEntities.contains(basicEntity));
    }

    @Test
    void createCanReturnAndSaveMultipleBasicEntitiesWithCustomAttributesToTheDatabase() {
        final int numberOfEntities = 2;

        final String customLongName = "myLongAttribute";
        final Long customLongValue = 999L;

        final String customStringName = "myStringAttribute";
        final String customStringValue = "a custom string value";

        final List<BasicEntity> basicEntities = basicEntityFactory.withAttributes(
                Map.of(
                        "myLongAttribute", new Attribute(customLongName, customLongValue),
                        "myStringAttribute", new Attribute(customStringName, customStringValue)
                )
        ).create(numberOfEntities);

        final List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertEquals(numberOfEntities, basicEntities.size());

        assertEquals(customLongValue, basicEntities.get(0).getMyLongAttribute());
        assertEquals(customStringValue, basicEntities.get(0).getMyStringAttribute());

        assertEquals(999L, basicEntities.get(1).getMyLongAttribute());
        assertEquals(customStringValue, basicEntities.get(1).getMyStringAttribute());

        assertEquals(numberOfEntities, savedEntities.size());
        assertTrue(savedEntities.contains(basicEntities.get(0)));
        assertTrue(savedEntities.contains(basicEntities.get(1)));
    }

    @Test
    void createThrowsAnExceptionIfCopiesIsLessThanOne() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> basicEntityFactory.create(0));
    }

    void assertBasicEntityCorrectlyMadeWithDefaultAttributes(final BasicEntity basicEntity) {
        assertNull(basicEntity.getId());
        assertTrue(basicEntity.getMyLongAttribute() instanceof Long);
        assertTrue(basicEntity.getMyStringAttribute() instanceof String);
    }

    void assertBasicEntityCorrectlyCreatedWithDefaultAttributes(final BasicEntity basicEntity) {
        assertTrue(basicEntity.getId() instanceof Long);
        assertTrue(basicEntity.getMyLongAttribute() instanceof Long);
        assertTrue(basicEntity.getMyStringAttribute() instanceof String);
    }
}
