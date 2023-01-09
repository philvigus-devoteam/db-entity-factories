package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.testfixtures.entities.ChildEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

/**
 * The Child entity factory used by tests for this library.
 */
@EntityFactory
@DependsOn("parentEntityFactory")
public class ChildEntityFactory extends AbstractBaseEntityFactory<ChildEntity> {
    /**
     * Instantiates a new Child entity factory.
     *
     * @param repository          the repository used to save instances of the entity
     * @param parentEntityFactory the parent entity factory used to create the child's parent entity
     */
    @Autowired
    public ChildEntityFactory(final JpaRepository<ChildEntity, Long> repository, ParentEntityFactory parentEntityFactory) {
        super(ChildEntity.class, repository, Map.of("parent", new DefaultAttribute<>("parent", parentEntityFactory::persist)));
    }
}
