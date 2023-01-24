package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.factories.BaseEntityFactory;
import com.philvigus.dbentityfactories.factories.HibernateEntityFactory;
import com.philvigus.dbentityfactories.testfixtures.entities.EntityWithUniqueAttributes;
import net.datafaker.Faker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

/**
 * The entity with unique attributes factory used by tests for this library.
 */
@EntityFactory
public class EntityWithUniqueAttributesFactory extends HibernateEntityFactory<EntityWithUniqueAttributes> {
    public static final String UNIQUE_ATTRIBUTE_NAME = "uniqueString";
    public static final String NORMAL_ATTRIBUTE_NAME = "repeatableString";
    private static final Faker faker = new Faker();


    /**
     * Instantiates a new Entity with unique attributes factory.
     *
     * @param repository the repository used to save instances of the entity
     */
    public EntityWithUniqueAttributesFactory(final JpaRepository<EntityWithUniqueAttributes, Long> repository) {
        super(EntityWithUniqueAttributes.class, repository);
    }

    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(final BaseEntityFactory<?>... dependentFactories) {
        return toAttributeMap(
                new DefaultAttribute<>(EntityWithUniqueAttributesFactory.UNIQUE_ATTRIBUTE_NAME, () -> EntityWithUniqueAttributesFactory.faker.lorem().sentence(), true),
                new DefaultAttribute<>(EntityWithUniqueAttributesFactory.NORMAL_ATTRIBUTE_NAME, () -> "This will be the same in different entities of this type")
        );
    }
}
