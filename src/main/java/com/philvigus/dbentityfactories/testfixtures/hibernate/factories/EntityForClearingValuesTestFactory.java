package com.philvigus.dbentityfactories.testfixtures.hibernate.factories;

import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.factories.BaseEntityFactory;
import com.philvigus.dbentityfactories.factories.HibernateEntityFactory;
import com.philvigus.dbentityfactories.testfixtures.entities.EntityWithUniqueAttributes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

@EntityFactory
public class EntityForClearingValuesTestFactory extends HibernateEntityFactory<EntityWithUniqueAttributes> {
    public static final String UNIQUE_STRING = "uniqueString";
    public static final String UNIQUE_LONG = "uniqueLong";

    /**
     * Instantiates a new Entity with unique attributes factory.
     *
     * @param repository the repository used to save instances of the entity
     */
    public EntityForClearingValuesTestFactory(final JpaRepository<EntityWithUniqueAttributes, Long> repository) {
        super(EntityWithUniqueAttributes.class, repository);
    }

    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(final BaseEntityFactory<?>... dependentFactories) {
        return toAttributeMap(
                new DefaultAttribute<>(EntityForClearingValuesTestFactory.UNIQUE_STRING, () -> "Not unique", true),
                new DefaultAttribute<>(EntityForClearingValuesTestFactory.UNIQUE_LONG, () -> 1L, true)
        );
    }
}
