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
public class BasicEntityFactory extends AbstractBaseEntityFactory<BasicEntity> {
    public static final String LONG_ATTRIBUTE_NAME = "myLongAttribute";
    public static final String STRING_ATTRIBUTE_NAME = "myStringAttribute";

    private static final Faker faker = new Faker();

    /**
     * Instantiates a new Basic entity factory.
     *
     * @param repository the repository used to save instances of the entity
     */
    public BasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository);
    }

    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(AbstractBaseEntityFactory<?>... dependentFactories) {
        return toAttributeMap(
                new DefaultAttribute<>(BasicEntityFactory.LONG_ATTRIBUTE_NAME, () -> BasicEntityFactory.faker.number().numberBetween(1L, 5L)),
                new DefaultAttribute<>(BasicEntityFactory.STRING_ATTRIBUTE_NAME, () -> BasicEntityFactory.faker.lorem().sentence())
        );
    }
}
