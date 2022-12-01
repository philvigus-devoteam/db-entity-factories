package com.philvigus.dbentityfactories;

import com.philvigus.dbentityfactories.attributes.CustomAttribute;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.exceptions.EntityFactoryException;
import net.datafaker.Faker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.apache.commons.beanutils.BeanUtils.setProperty;

public abstract class AbstractBaseEntityFactory<T> {
    protected static final Faker faker = new Faker();
    protected final Class<T> entityClass;
    protected final JpaRepository<T, Long> repository;

    protected Map<String, CustomAttribute<?>> customAttributes;
    protected Map<String, DefaultAttribute<?>> sortedDefaultAttributes;

    protected AbstractBaseEntityFactory(final Class<T> entityClass, final JpaRepository<T, Long> repository, final Map<String, DefaultAttribute<?>> defaultAttributes) {
        this.entityClass = entityClass;
        this.repository = repository;

        this.sortedDefaultAttributes = defaultAttributes;
        this.customAttributes = new ConcurrentHashMap<>();
    }

    @Transactional
    public List<T> create(final int copies) {
        if (copies < 1) {
            throw new IllegalArgumentException("copies must be greater than 0");
        }

        final List<T> entities = new ArrayList<>(copies);

        IntStream.range(0, copies).forEach(i -> entities.add(create()));

        return entities;
    }

    public T create() {
        return repository.save(getEntityWithAttributesSet(customAttributes));
    }

    public List<T> make(final int copies) {
        if (copies < 1) {
            throw new IllegalArgumentException("copies must be greater than 0");
        }

        final List<T> entities = new ArrayList<>(copies);

        IntStream.range(0, copies).forEach(i -> entities.add(make()));

        return entities;
    }

    public T make() {
        return getEntityWithAttributesSet(customAttributes);
    }

    public AbstractBaseEntityFactory<T> withAttributes(final Map<String, CustomAttribute<?>> customAttributes) {
        this.customAttributes = customAttributes;

        return this;
    }

    protected T getEntityWithAttributesSet(final Map<String, CustomAttribute<?>> customAttributes) {
        final T entity = instantiateEntity();

        return setEntityAttributes(entity, customAttributes);
    }

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

    protected T setEntityAttributes(final T entity, final Map<String, CustomAttribute<?>> customAttributes) {
        final List<String> customAttributesSet = new ArrayList<>();

        setCustomAttributesOnEntity(customAttributes, customAttributesSet, entity);
        setDefaultAttributesOnEntity(customAttributesSet, entity);

        return entity;
    }

    private void setCustomAttributesOnEntity(final Map<String, CustomAttribute<?>> customAttributes, final List<String> customAttributesSet, final T entity) {
        customAttributes.forEach((name, customAttribute) -> {
            Object customValue;

            if (sortedDefaultAttributes.containsKey(name) && sortedDefaultAttributes.get(name).isUnique()) {
                customValue = getUniqueCustomValue(customAttribute);
                sortedDefaultAttributes.get(name).addUsedValue(customValue);
            } else {
                customValue = customAttribute.getValue();
            }

            setEntityAttribute(entity, name, customValue);
            customAttributesSet.add(name);
        });
    }

    private void setDefaultAttributesOnEntity(final List<String> customAttributesSet, final T entity) {
        sortedDefaultAttributes.forEach((name, defaultAttribute) -> {
            if (!customAttributesSet.contains(name)) {
                setEntityAttribute(entity, name, defaultAttribute.getValue());
            }
        });
    }

    protected void setEntityAttribute(final T entity, final String name, final Object value) {
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
            if (attempts == DefaultAttribute.MAX_UNIQUE_ATTEMPTS) {
                throw new EntityFactoryException(String.format(
                        "Unable to find unique value for attribute %s after %s attempts",
                        customAttribute.getName(),
                        DefaultAttribute.MAX_UNIQUE_ATTEMPTS)
                );
            }

            customValue = customAttribute.getValue();
            attempts++;
        } while (sortedDefaultAttributes.get(customAttribute.getName()).hasUsedValue(customValue));

        return customValue;
    }
}
