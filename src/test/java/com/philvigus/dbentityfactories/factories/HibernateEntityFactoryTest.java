package com.philvigus.dbentityfactories.factories;

import com.philvigus.dbentityfactories.attributes.CustomAttribute;
import com.philvigus.dbentityfactories.exceptions.EntityFactoryException;
import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import com.philvigus.dbentityfactories.testfixtures.entities.ChildEntity;
import com.philvigus.dbentityfactories.testfixtures.entities.EntityWithUniqueAttributes;
import com.philvigus.dbentityfactories.testfixtures.entities.ParentEntity;
import com.philvigus.dbentityfactories.testfixtures.hibernate.factories.*;
import com.philvigus.dbentityfactories.testfixtures.hibernate.repositories.BasicEntityRepository;
import com.philvigus.dbentityfactories.testfixtures.hibernate.repositories.ChildEntityRepository;
import com.philvigus.dbentityfactories.testfixtures.hibernate.repositories.EntityWithUniqueAttributesRepository;
import com.philvigus.dbentityfactories.testfixtures.hibernate.repositories.ParentEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class HibernateEntityFactoryTest {
    @Autowired
    BasicEntityFactory basicEntityFactory;

    @Autowired
    EntityWithUniqueAttributesFactory entityWithUniqueAttributesFactory;

    @Autowired
    ChildEntityFactory childEntityFactory;

    @Autowired
    BasicEntityRepository basicEntityRepository;

    @Autowired
    EntityWithUniqueAttributesRepository entityWithUniqueAttributesRepository;

    @Autowired
    ChildEntityRepository childEntityRepository;
    @Autowired
    BrokenBasicEntityFactory brokenBasicEntityFactory;

    @Autowired
    EntityForClearingValuesTestFactory entityForClearingValuesTestFactory;

    @Autowired
    private ParentEntityRepository parentEntityRepository;

    @Test
    void makeCanReturnASingleBasicEntityWithItsAttributesCorrectlySet() {
        final BasicEntity basicEntity = basicEntityFactory.create();

        assertBasicEntityCorrectlyMadeWithDefaultAttributes(basicEntity);
    }

    @Test
    void makeCanReturnAListOfMultipleBasicEntitiesWithTheirAttributesCorrectlySet() {
        final int numberOfEntities = 2;

        final List<BasicEntity> basicEntities = basicEntityFactory.create(numberOfEntities);

        assertEquals(numberOfEntities, basicEntities.size());
        assertBasicEntityCorrectlyMadeWithDefaultAttributes(basicEntities.get(0));
        assertBasicEntityCorrectlyMadeWithDefaultAttributes(basicEntities.get(1));
    }

    @Test
    void makeCanReturnASingleBasicEntityWithCustomAttributesSet() {
        final Long customLongValue = 999L;
        final String customStringValue = "a custom string value";

        final BasicEntity basicEntity = basicEntityFactory.withCustomAttributes(
                new CustomAttribute<>(BasicEntityFactory.LONG_ATTRIBUTE_NAME, () -> customLongValue),
                new CustomAttribute<>(BasicEntityFactory.STRING_ATTRIBUTE_NAME, () -> customStringValue)
        ).create();

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

        final List<BasicEntity> basicEntities = basicEntityFactory.withCustomAttributes(
                new CustomAttribute<>(customLongName, () -> customLongValue),
                new CustomAttribute<>(customStringName, () -> customStringValue)
        ).create(numberOfEntities);

        assertNull(basicEntities.get(0).getId());
        assertEquals(customLongValue, basicEntities.get(0).getMyLongAttribute());
        assertEquals(customStringValue, basicEntities.get(0).getMyStringAttribute());

        assertNull(basicEntities.get(1).getId());
        assertEquals(customLongValue, basicEntities.get(1).getMyLongAttribute());
        assertEquals(customStringValue, basicEntities.get(1).getMyStringAttribute());
    }

    @Test
    void makeThrowsAnExceptionIfCopiesIsLessThanOne() {
        assertThrows(IllegalArgumentException.class, () -> basicEntityFactory.create(0));
    }

    @Test
    void createCanReturnAndSaveASingleBasicEntityWithItsAttributesCorrectlySetToTheDatabase() {
        final BasicEntity basicEntity = basicEntityFactory.persist();
        final List<BasicEntity> savedEntities = basicEntityRepository.findAll();

        assertBasicEntityCorrectlyCreatedWithDefaultAttributes(basicEntity);

        assertEquals(1, savedEntities.size());
        assertTrue(savedEntities.contains(basicEntity));
    }

    @Test
    void createCanReturnAndSaveMultipleBasicEntitiesWithTheirAttributesCorrectlySetToTheDatabase() {
        final int numberOfEntities = 2;

        final List<BasicEntity> basicEntities = basicEntityFactory.persist(numberOfEntities);
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

        final BasicEntity basicEntity = basicEntityFactory.withCustomAttributes(
                new CustomAttribute<>(customLongName, () -> customLongValue),
                new CustomAttribute<>(customStringName, () -> customStringValue)
        ).persist();

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

        final List<BasicEntity> basicEntities = basicEntityFactory.withCustomAttributes(
                new CustomAttribute<>(customLongName, () -> customLongValue),
                new CustomAttribute<>(customStringName, () -> customStringValue)
        ).persist(numberOfEntities);

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
        assertThrows(IllegalArgumentException.class, () -> basicEntityFactory.persist(0));
    }

    @Test
    void factoriesCanCreateMultipleEntitiesWithUniqueDefaultAttributesCorrectly() {
        final int numberOfEntities = 3;

        final List<EntityWithUniqueAttributes> createdEntities = entityWithUniqueAttributesFactory.persist(numberOfEntities);
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
                .withCustomAttributes(
                        new CustomAttribute<>("uniqueString", () -> {
                            final List<String> list = Arrays.asList("bob", "eric", "steve");
                            final Random rand = new Random();

                            return list.get(rand.nextInt(list.size()));
                        })
                )
                .persist(numberOfEntities);
        final List<EntityWithUniqueAttributes> savedEntities = entityWithUniqueAttributesRepository.findAll();

        assertEquals(numberOfEntities, createdEntities.size());
        assertEquals(numberOfEntities, savedEntities.size());

        assertNotEquals(savedEntities.get(0).getUniqueString(), savedEntities.get(1).getUniqueString());
        assertNotEquals(savedEntities.get(0).getUniqueString(), savedEntities.get(1).getUniqueString());
        assertNotEquals(savedEntities.get(1).getUniqueString(), savedEntities.get(2).getUniqueString());
    }

    @Test
    void parentEntitiesAreCreatedAutomaticallyWhenChildEntitiesHaveAManyToOneRelationship() {
        final ChildEntity childEntity = childEntityFactory.persist();

        final List<ParentEntity> savedParentEntities = parentEntityRepository.findAll();
        final List<ChildEntity> childEntities = childEntityRepository.findAll();

        assertEquals(1, savedParentEntities.size());
        assertEquals(1, childEntities.size());
        assertEquals(savedParentEntities.get(0).getId(), childEntity.getParent().getId());
    }

    @Test
    void specifyingADefaultAttributeWithANameThatDoesntExistOnAnEntityThrowsAnException() {
        final String incorrectAttributeName = "I do not exist";
        final Long customLongValue = 999L;

        assertThrows(EntityFactoryException.class, () -> basicEntityFactory.withCustomAttributes(
                new CustomAttribute<>(incorrectAttributeName, () -> customLongValue)
        ).create());
    }

    @Test
    void specifyingACustomAttributeWithANameThatDoesntExistOnAnEntityThrowsAnException() {
        assertThrows(EntityFactoryException.class, () -> brokenBasicEntityFactory.persist());
    }

    @Test
    void youCanResetTheUsedValuesOnAllAttributes() {
        entityForClearingValuesTestFactory.create();

        entityForClearingValuesTestFactory.clearAllUsedAttributeValues();

        assertDoesNotThrow(() -> entityForClearingValuesTestFactory.create());
    }

    @Test
    void youCanResetTheUsedValuesOnSpecificAttribute() {
        entityForClearingValuesTestFactory.create();

        entityForClearingValuesTestFactory.clearUsedValuesForAttribute(EntityForClearingValuesTestFactory.UNIQUE_LONG);

        assertDoesNotThrow(() -> entityForClearingValuesTestFactory
                .withCustomAttributes(new CustomAttribute<>(
                        EntityForClearingValuesTestFactory.UNIQUE_STRING,
                        () -> "a different value"))
                .create());

        assertThrows(EntityFactoryException.class, () -> entityForClearingValuesTestFactory.create());

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
