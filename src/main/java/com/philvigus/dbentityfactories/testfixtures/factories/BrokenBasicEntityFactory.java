package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import net.datafaker.Faker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

/**
 * The Basic entity factory used by tests for this library.
 */
@EntityFactory
public class BrokenBasicEntityFactory extends AbstractBaseEntityFactory<BasicEntity> {
    public static final String INCORRECT_ATTRIBUTE_NAME = "iDoNotExist";
    public static final String STRING_ATTRIBUTE_NAME = "myStringAttribute";

    private static final Faker faker = new Faker();

    /**
     * Instantiates a new Basic entity factory.
     *
     * @param repository the repository used to save instances of the entity
     */
    public BrokenBasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository);
    }

    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(final AbstractBaseEntityFactory<?>... dependentFactories) {
        return toAttributeMap(
                new DefaultAttribute<>(BrokenBasicEntityFactory.INCORRECT_ATTRIBUTE_NAME, () -> BrokenBasicEntityFactory.faker.number().numberBetween(1L, 5L)),
                new DefaultAttribute<>(BrokenBasicEntityFactory.STRING_ATTRIBUTE_NAME, () -> BrokenBasicEntityFactory.faker.lorem().sentence())
        );
    }
}
