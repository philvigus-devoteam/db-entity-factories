package com.philvigus.dbentityfactories.factories;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The base entity factory.
 *
 * @param <T> the type of the entity the factory creates
 */
public class HibernateEntityFactory<T> extends BaseEntityFactory<T> {
    /**
     * The repository used to save instances of the entity.
     */
    protected final JpaRepository<T, Long> repository;

    /**
     * Instantiates a new base entity factory.
     *
     * @param entityClass        the entity class
     * @param repository         the repository used to save instances of the entity
     * @param dependentFactories any factories the creation of this entity depends on
     */
    protected HibernateEntityFactory(
            final Class<T> entityClass,
            final JpaRepository<T, Long> repository,
            final BaseEntityFactory<?>... dependentFactories) {
        super(entityClass, dependentFactories);

        this.repository = repository;
    }

    /**
     * Creates and saves an individual entity.
     *
     * @return the created and saved entity
     */
    @Override
    public T persist() {
        return repository.save(getEntityWithAttributesSet(customAttributes));
    }
}
