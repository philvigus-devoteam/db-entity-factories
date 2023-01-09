package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.testfixtures.entities.EntityWithUniqueAttributes;
import net.datafaker.Faker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

/**
 * The entity with unique attributes factory used by tests for this library.
 */
@EntityFactory
public class EntityWithUniqueAttributesFactory extends AbstractBaseEntityFactory<EntityWithUniqueAttributes> {
    private static final Faker faker = new Faker();

    /**
     * Instantiates a new Entity with unique attributes factory.
     *
     * @param repository the repository used to save instances of the entity
     */
    public EntityWithUniqueAttributesFactory(final JpaRepository<EntityWithUniqueAttributes, Long> repository) {
        super(EntityWithUniqueAttributes.class, repository, Map.of(
                "uniqueString", new DefaultAttribute<>("uniqueString", () -> EntityWithUniqueAttributesFactory.faker.lorem().sentence(), true),
                "repeatableString", new DefaultAttribute<>("repeatableString", () -> "This can be the same in different entities of this type")
        ));
    }
}
