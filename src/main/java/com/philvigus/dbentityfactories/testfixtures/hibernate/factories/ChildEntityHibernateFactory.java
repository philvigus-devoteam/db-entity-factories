package com.philvigus.dbentityfactories.testfixtures.hibernate.factories;

import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.factories.BaseEntityFactory;
import com.philvigus.dbentityfactories.factories.HibernateEntityFactory;
import com.philvigus.dbentityfactories.testfixtures.entities.ChildEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

/**
 * The Child entity factory used by tests for this library.
 */
@EntityFactory
public class ChildEntityHibernateFactory extends HibernateEntityFactory<ChildEntity> {
    public static final String PARENT_ATTRIBUTE_NAME = "parent";

    /**
     * Instantiates a new Child entity factory.
     *
     * @param repository          the repository used to save instances of the entity
     * @param parentEntityFactory the parent entity factory used to create the child's parent entity
     */
    @Autowired
    public ChildEntityHibernateFactory(final JpaRepository<ChildEntity, Long> repository, final ParentEntityHibernateFactory parentEntityFactory) {
        super(ChildEntity.class, repository, parentEntityFactory);
    }

    @Override
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(final BaseEntityFactory<?>... dependentFactories) {
        return toAttributeMap(
                new DefaultAttribute<>(ChildEntityHibernateFactory.PARENT_ATTRIBUTE_NAME, dependentFactories[0]::persist)
        );
    }
}
