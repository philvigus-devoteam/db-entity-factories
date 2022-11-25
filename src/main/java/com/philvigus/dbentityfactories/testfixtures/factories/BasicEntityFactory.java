package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.Attribute;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@EntityFactory
public class BasicEntityFactory extends AbstractBaseEntityFactory<BasicEntity> {
    public BasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository);

        defaultAttributes.put("myLongAttribute", new Attribute<>("myLongAttribute",
                faker.number().numberBetween(1L, 5L)));

        defaultAttributes.put("myStringAttribute", new Attribute<>("myStringAttribute", faker.lorem().sentence()));
    }
}
