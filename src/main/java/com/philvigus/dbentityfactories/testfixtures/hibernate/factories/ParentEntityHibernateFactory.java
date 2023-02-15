package com.philvigus.dbentityfactories.testfixtures.hibernate.factories;

import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.factories.HibernateEntityFactory;
import com.philvigus.dbentityfactories.testfixtures.entities.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The Parent entity factory used by tests for this library.
 */
@EntityFactory
public class ParentEntityHibernateFactory extends HibernateEntityFactory<ParentEntity> {
    /**
     * Instantiates a new Parent entity factory.
     *
     * @param repository the repository used to save instances of the entity
     */
    public ParentEntityHibernateFactory(final JpaRepository<ParentEntity, Long> repository) {
        super(ParentEntity.class, repository);
    }
}
