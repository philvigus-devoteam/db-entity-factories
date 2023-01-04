package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.attributes.DefaultAttribute;
import com.philvigus.dbentityfactories.testfixtures.entities.ChildEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

@EntityFactory
@DependsOn("parentEntityFactory")
public class ChildEntityFactory extends AbstractBaseEntityFactory<ChildEntity> {
    @Autowired
    public ChildEntityFactory(final JpaRepository<ChildEntity, Long> repository, ParentEntityFactory parentEntityFactory) {
        super(ChildEntity.class, repository, null);

        this.sortedDefaultAttributes = Map.of("parent", new DefaultAttribute<>("parent", parentEntityFactory::create));
    }
}
