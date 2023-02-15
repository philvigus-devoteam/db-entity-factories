package com.philvigus.dbentityfactories.testfixtures.jdbc.factories;

import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.factories.BaseEntityFactory;
import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import com.philvigus.dbentityfactories.testfixtures.jdbc.repositories.BasicEntityJdbcRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * The JDBC Basic entity factory used by tests for this library.
 */
@EntityFactory
public class BasicEntityJdbcFactory extends BaseEntityFactory<BasicEntity> {
    public static final String LONG_ATTRIBUTE_NAME = "myLongAttribute";
    public static final String STRING_ATTRIBUTE_NAME = "myStringAttribute";

    private static final Faker faker = new Faker();
    /**
     * The repository used to save instances of the entity.
     */
    private final BasicEntityJdbcRepository basicEntityJdbcRepository;

    /**
     * Instantiates a new base entity factory.
     *
     * @param repository         the repository used to save instances of the entity
     * @param dependentFactories any factories the creation of this entity depends on
     */
    @Autowired
    protected BasicEntityJdbcFactory(
            BasicEntityJdbcRepository repository, BaseEntityFactory<?>... dependentFactories) {
        super(BasicEntity.class, dependentFactories);

        this.basicEntityJdbcRepository = repository;
    }

    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(final BaseEntityFactory<?>... dependentFactories) {
        return toAttributeMap(
                new DefaultAttribute<>(BasicEntityJdbcFactory.LONG_ATTRIBUTE_NAME, () -> BasicEntityJdbcFactory.faker.number().numberBetween(1L, 5L)),
                new DefaultAttribute<>(BasicEntityJdbcFactory.STRING_ATTRIBUTE_NAME, () -> BasicEntityJdbcFactory.faker.lorem().sentence())
        );
    }

    /**
     * Creates and saves an individual entity.
     *
     * @return the created and saved entity
     */
    @Override
    public BasicEntity persist() {
        return basicEntityJdbcRepository.save(getEntityWithAttributesSet(customAttributes));
    }
}
