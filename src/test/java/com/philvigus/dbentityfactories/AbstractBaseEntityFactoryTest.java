package com.philvigus.dbentityfactories;

import com.philvigus.dbentityfactories.attributes.CustomAttribute;
import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import com.philvigus.dbentityfactories.testfixtures.entities.EntityWithUniqueAttributes;
import com.philvigus.dbentityfactories.testfixtures.factories.BasicEntityFactory;
import com.philvigus.dbentityfactories.testfixtures.factories.EntityWithUniqueAttributesFactory;
import com.philvigus.dbentityfactories.testfixtures.repositories.BasicEntityRepository;
import com.philvigus.dbentityfactories.testfixtures.repositories.EntityWithUniqueAttributesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class AbstractBaseEntityFactoryTest {
    BasicEntityFactory basicEntityFactory;
    EntityWithUniqueAttributesFactory entityWithUniqueAttributesFactory;

    @Autowired
    BasicEntityRepository basicEntityRepository;

    @Autowired
    EntityWithUniqueAttributesRepository entityWithUniqueAttributesRepository;

    @BeforeEach
    void setUp() {
        basicEntityFactory = new BasicEntityFactory(basicEntityRepository);
        entityWithUniqueAttributesFactory = new EntityWithUniqueAttributesFactory(entityWithUniqueAttributesRepository);
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
                        customLongName, new CustomAttribute<>(customLongName, () -> customLongValue),
                        customStringName, new CustomAttribute<>(customStringName, () -> customStringValue)
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
                        customLongName, new CustomAttribute<>(customLongName, () -> customLongValue),
                        customStringName, new CustomAttribute<>(customStringName, () -> customStringValue)
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
        assertThrows(IllegalArgumentException.class, () -> basicEntityFactory.make(0));
    }

    @Test
    void createCanReturnAndSaveASingleBasicEntityWithItsAttributesCorrectlySetToTheDatabase() {
        final BasicEntity basicEntity = basicEntityFactory.create();
        final List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertBasicEntityCorrectlyCreatedWithDefaultAttributes(basicEntity);

        assertEquals(1, savedEntities.size());
        assertTrue(savedEntities.stream().anyMatch(o -> Objects.equals(o.getId(), basicEntity.getId())));
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
        assertTrue(savedEntities.stream().anyMatch(o -> Objects.equals(o.getId(), basicEntities.get(0).getId())));
        assertTrue(savedEntities.stream().anyMatch(o -> Objects.equals(o.getId(), basicEntities.get(1).getId())));
    }

    @Test
    void createCanReturnAndSaveASingleBasicEntityWithCustomAttributesToTheDatabase() {
        final String customLongName = "myLongAttribute";
        final Long customLongValue = 999L;

        final String customStringName = "myStringAttribute";
        final String customStringValue = "a custom string value";

        final BasicEntity basicEntity = basicEntityFactory.withAttributes(
                Map.of(
                        customLongName, new CustomAttribute<>(customLongName, () -> customLongValue),
                        customStringName, new CustomAttribute<>(customStringName, () -> customStringValue)
                )
        ).create();

        final List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertEquals(customLongValue, basicEntity.getMyLongAttribute());
        assertEquals(customStringValue, basicEntity.getMyStringAttribute());

        assertEquals(1, savedEntities.size());
        assertTrue(savedEntities.stream().anyMatch(o -> Objects.equals(o.getId(), basicEntity.getId())));
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
                        customLongName, new CustomAttribute<>(customLongName, () -> customLongValue),
                        customStringName, new CustomAttribute<>(customStringName, () -> customStringValue)
                )
        ).create(numberOfEntities);

        final List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertEquals(numberOfEntities, basicEntities.size());

        assertEquals(customLongValue, basicEntities.get(0).getMyLongAttribute());
        assertEquals(customStringValue, basicEntities.get(0).getMyStringAttribute());

        assertEquals(999L, basicEntities.get(1).getMyLongAttribute());
        assertEquals(customStringValue, basicEntities.get(1).getMyStringAttribute());

        assertEquals(numberOfEntities, savedEntities.size());
        assertTrue(savedEntities.stream().anyMatch(o -> Objects.equals(o.getId(), basicEntities.get(0).getId())));
        assertTrue(savedEntities.stream().anyMatch(o -> Objects.equals(o.getId(), basicEntities.get(1).getId())));
    }

    @Test
    void createThrowsAnExceptionIfCopiesIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> basicEntityFactory.create(0));
    }

    @Test
    void factoriesCanCreateMultipleEntitiesWithUniqueDefaultAttributesCorrectly() {
        final int numberOfEntities = 3;

        final List<EntityWithUniqueAttributes> createdEntities = entityWithUniqueAttributesFactory.create(numberOfEntities);
        final List<EntityWithUniqueAttributes> savedEntities = entityWithUniqueAttributesRepository.findAll();

        assertEquals(numberOfEntities, createdEntities.size());
        assertEquals(numberOfEntities, savedEntities.size());
        assertNotEquals(savedEntities.get(0).getUniqueString(), savedEntities.get(1).getUniqueString());
        assertNotEquals(savedEntities.get(0).getUniqueString(), savedEntities.get(1).getUniqueString());
        assertNotEquals(savedEntities.get(1).getUniqueString(), savedEntities.get(2).getUniqueString());
    }

    @Test
    void factoriesCanCreateMultipleEntitiesWithUniqueCustomAttributesCorrectly() {
        final int numberOfEntities = 3;

        final List<EntityWithUniqueAttributes> createdEntities = entityWithUniqueAttributesFactory
                .withAttributes(
                        Map.of(
                                "uniqueString", new CustomAttribute<>("uniqueString", () -> {
                                    final List<String> list = Arrays.asList("bob", "eric", "steve");
                                    final Random rand = new Random();

                                    return list.get(rand.nextInt(list.size()));
                                })
                        )
                )
                .create(numberOfEntities);
        final List<EntityWithUniqueAttributes> savedEntities = entityWithUniqueAttributesRepository.findAll();

        assertEquals(numberOfEntities, createdEntities.size());
        assertEquals(numberOfEntities, savedEntities.size());
        assertNotEquals(savedEntities.get(0).getUniqueString(), savedEntities.get(1).getUniqueString());
        assertNotEquals(savedEntities.get(0).getUniqueString(), savedEntities.get(1).getUniqueString());
        assertNotEquals(savedEntities.get(1).getUniqueString(), savedEntities.get(2).getUniqueString());
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
