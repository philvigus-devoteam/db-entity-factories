package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.testfixtures.entities.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

/**
 * The Parent entity factory used by tests for this library.
 */
@EntityFactory
public class ParentEntityFactory extends AbstractBaseEntityFactory<ParentEntity> {
    public static final String STRING_ATTRIBUTE_NAME = "stringAttribute";

    /**
     * Instantiates a new Parent entity factory.
     *
     * @param repository the repository used to save instances of the entity
     */
    public ParentEntityFactory(final JpaRepository<ParentEntity, Long> repository) {
        super(ParentEntity.class, repository);
    }

    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(AbstractBaseEntityFactory<?>... dependentFactories) {
        return toAttributeMap(
                new DefaultAttribute<>(ParentEntityFactory.STRING_ATTRIBUTE_NAME, () -> "string value")
        );
    }
}
