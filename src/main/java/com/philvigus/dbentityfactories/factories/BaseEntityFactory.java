package com.philvigus.dbentityfactories.factories;

import com.philvigus.dbentityfactories.attributes.CustomAttribute;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.exceptions.EntityFactoryException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.apache.commons.beanutils.BeanUtils.setProperty;
import static org.apache.commons.beanutils.PropertyUtils.isReadable;
import static org.apache.commons.beanutils.PropertyUtils.isWriteable;

/**
 * The base entity factory.
 *
 * @param <T> the type of the entity the factory creates
 */
public abstract class BaseEntityFactory<T> {

    /**
     * The class of the entity the factory creates.
     */
    protected final Class<T> entityClass;

    /**
     * The repository used to save instances of the entity.
     */
    protected final JpaRepository<T, Long> repository;

    /**
     * A list of custom attributes used to override default attributes when creating entities.
     */
    protected Map<String, CustomAttribute<?>> customAttributes;

    /**
     * Default attributes used when creating entities.
     */
    protected Map<String, DefaultAttribute<?>> defaultAttributes;

    /**
     * Instantiates a new base entity factory.
     *
     * @param entityClass        the entity class
     * @param repository         the repository used to save instances of the entity
     * @param dependentFactories any factories the creation of this entity depends on
     */
    protected BaseEntityFactory(
            final Class<T> entityClass,
            final JpaRepository<T, Long> repository,
            final BaseEntityFactory<?>... dependentFactories) {
        this.entityClass = entityClass;
        this.repository = repository;

        this.customAttributes = new ConcurrentHashMap<>();
        this.defaultAttributes = getDefaultAttributes(dependentFactories);
    }

    /**
     * Gets the default attributes for the entity created by this factory.
     *
     * @param dependentFactories any factories the creation of this entity depends on
     * @return the default attributes
     */
    protected Map<String, DefaultAttribute<?>> getDefaultAttributes(final BaseEntityFactory<?>... dependentFactories) {
        return Map.of();
    }

    /**
     * Converts a list of default attributes to a map, with the keys being the name of each attribute
     * and the values being the attributes themselves.
     *
     * @param attributes the default attributes
     * @return the attribute map
     */
    protected Map<String, DefaultAttribute<?>> toAttributeMap(final DefaultAttribute<?>... attributes) {
        final Map<String, DefaultAttribute<?>> newDefaultAttributes = new ConcurrentHashMap<>();

        for (final DefaultAttribute<?> defaultAttribute : attributes) {
            newDefaultAttributes.put(defaultAttribute.getName(), defaultAttribute);
        }

        return newDefaultAttributes;
    }

    /**
     * Creates and saves a specified number of entities.
     *
     * @param copies the number of entities to save
     * @return the created and saved list of entities
     */
    @Transactional
    public List<T> persist(final int copies) {
        if (copies < 1) {
            throw new IllegalArgumentException("copies must be greater than 0");
        }

        final List<T> entities = new ArrayList<>(copies);

        IntStream.range(0, copies).forEach(i -> entities.add(persist()));

        return entities;
    }

    /**
     * Creates and saves an individual entity.
     *
     * @return the created and saved entity
     */
    public T persist() {
        return repository.save(getEntityWithAttributesSet(customAttributes));
    }

    /**
     * Creates a specified number of entities.
     *
     * @param copies the number of entities to create
     * @return the created list of entities
     */
    public List<T> create(final int copies) {
        if (copies < 1) {
            throw new IllegalArgumentException("copies must be greater than 0");
        }

        final List<T> entities = new ArrayList<>(copies);

        IntStream.range(0, copies).forEach(i -> entities.add(create()));

        return entities;
    }

    /**
     * Creates an individual entity.
     *
     * @return the created entity
     */
    public T create() {
        return getEntityWithAttributesSet(customAttributes);
    }

    /**
     * Allows the user to specify custom attributes to override the defaults with.
     *
     * @param customAttributes the custom attributes to use
     * @return the base entity factory using the custom attributes
     */
    public BaseEntityFactory<T> withCustomAttributes(final CustomAttribute<?>... customAttributes) {
        for (final CustomAttribute<?> customAttribute : customAttributes) {
            this.customAttributes.put(customAttribute.getName(), customAttribute);
        }

        return this;
    }

    public void clearAllUsedAttributeValues() {
        defaultAttributes.forEach((attributeName, defaultAttribute) -> defaultAttribute.clearUsedValues());
    }

    public void clearUsedValuesForAttribute(String attributeName) {
        defaultAttributes.get(attributeName).clearUsedValues();
    }

    /**
     * Gets an entity with attributes set.
     *
     * @param customAttributes the custom attributes to use when setting its attributes
     * @return the entity with attributes set
     */
    protected T getEntityWithAttributesSet(final Map<String, CustomAttribute<?>> customAttributes) {
        final T entity = instantiateEntity();

        return setEntityAttributes(entity, customAttributes);
    }

    /**
     * Instantiates an instance of the entity.
     *
     * @return the instantiated entity
     */
    protected T instantiateEntity() {
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException
                 | IllegalAccessException
                 | IllegalArgumentException
                 | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new EntityFactoryException(String.format("Unable to instantiate entity of type %s", entityClass), e);
        }
    }

    /**
     * Sets an entity's attributes.
     *
     * @param entity           the entity to set the attributes on
     * @param customAttributes the custom attributes to use when setting the attributes
     * @return the entity with its attributes set
     */
    protected T setEntityAttributes(final T entity, final Map<String, CustomAttribute<?>> customAttributes) {
        // custom attribute values are set first. We track the names of those that are set so that we don't overwrite
        // them with default attribute values
        final List<String> customAttributesSet = new ArrayList<>();

        setCustomAttributesOnEntity(customAttributes, customAttributesSet, entity);
        setDefaultAttributesOnEntity(customAttributesSet, entity);

        return entity;
    }

    private void setCustomAttributesOnEntity(final Map<String, CustomAttribute<?>> customAttributes, final List<String> customAttributesSet, final T entity) {
        customAttributes.forEach((name, customAttribute) -> {
            Object customValue;

            // if we have a default attribute with this name, and it's unique, we need to make sure the value
            // we assign it is added to the default attribute's used values
            if (defaultAttributes.containsKey(name) && defaultAttributes.get(name).isUnique()) {
                customValue = getUniqueCustomValue(customAttribute);
                defaultAttributes.get(name).addUsedValue(customValue);
            } else {
                customValue = customAttribute.getValue();
            }

            setEntityAttribute(entity, name, customValue);
            customAttributesSet.add(name);
        });
    }

    private void setDefaultAttributesOnEntity(final List<String> customAttributesSet, final T entity) {
        defaultAttributes.forEach((name, defaultAttribute) -> {
            // If we've already set this attribute with a custom value, we don't want to overwrite it
            if (!customAttributesSet.contains(name)) {
                setEntityAttribute(entity, name, defaultAttribute.getValue());
            }
        });
    }

    /**
     * Sets an entity's attribute value.
     *
     * @param entity the entity
     * @param name   the attribute name
     * @param value  the attribute value
     */
    protected void setEntityAttribute(final T entity, final String name, final Object value) {
        if (!propertyExists(entity, name)) {
            throw new EntityFactoryException(
                    String.format("Property %s does not exist on entity of type %s", name, entityClass));
        }

        try {
            setProperty(entity, name, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EntityFactoryException(
                    String.format("Unable to set property %s to %s on entity of type %s", name, value, entityClass), e);
        }
    }

    private Object getUniqueCustomValue(final CustomAttribute<?> customAttribute) {
        int attempts = 0;
        Object customValue;

        do {
            // bail out and throw an error after a set number of attempts to find a unique value
            if (attempts == DefaultAttribute.MAX_UNIQUE_ATTEMPTS) {
                throw new EntityFactoryException(String.format(
                        "Unable to find unique value for attribute %s after %s attempts",
                        customAttribute.getName(),
                        DefaultAttribute.MAX_UNIQUE_ATTEMPTS)
                );
            }

            customValue = customAttribute.getValue();
            attempts++;
        } while (defaultAttributes.get(customAttribute.getName()).hasUsedValue(customValue));

        return customValue;
    }

    private boolean propertyExists(Object entity, String propertyName) {
        return isReadable(entity, propertyName) && isWriteable(entity, propertyName);
    }
}
