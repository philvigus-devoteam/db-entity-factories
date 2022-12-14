package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.testfixtures.entities.ChildEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Child entity factory used by tests for this library.
 */
@EntityFactory
public class ChildEntityFactory extends AbstractBaseEntityFactory<ChildEntity> {
    public static final String PARENT_ATTRIBUTE_NAME = "parent";

    /**
     * Instantiates a new Child entity factory.
     *
     * @param repository          the repository used to save instances of the entity
     * @param parentEntityFactory the parent entity factory used to create the child's parent entity
     */
    @Autowired
    public ChildEntityFactory(final JpaRepository<ChildEntity, Long> repository, ParentEntityFactory parentEntityFactory) {
        super(
                ChildEntity.class,
                repository,
                new DefaultAttribute<>(ChildEntityFactory.PARENT_ATTRIBUTE_NAME, parentEntityFactory::persist)
        );
    }
}
