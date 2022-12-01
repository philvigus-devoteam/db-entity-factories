package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.testfixtures.entities.EntityWithUniqueAttributes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

@EntityFactory
public class EntityWithUniqueAttributesFactory extends AbstractBaseEntityFactory<EntityWithUniqueAttributes> {
    public EntityWithUniqueAttributesFactory(final JpaRepository<EntityWithUniqueAttributes, Long> repository) {
        super(EntityWithUniqueAttributes.class, repository, Map.of(
                "uniqueString", new DefaultAttribute<>("uniqueString", () -> AbstractBaseEntityFactory.faker.lorem().sentence(), true),
                "repeatableString", new DefaultAttribute<>("repeatableString", () -> "This can be the same in different entities of this type")
        ));
    }
}
