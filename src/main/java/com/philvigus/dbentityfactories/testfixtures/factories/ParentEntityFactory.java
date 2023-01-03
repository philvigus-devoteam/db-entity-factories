package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.testfixtures.entities.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

@EntityFactory
public class ParentEntityFactory extends AbstractBaseEntityFactory<ParentEntity> {
    public ParentEntityFactory(final JpaRepository<ParentEntity, Long> repository) {
        super(ParentEntity.class, repository, Map.of("stringAttribute", new DefaultAttribute<>("stringAttribute", () -> "string value")));
    }
}
