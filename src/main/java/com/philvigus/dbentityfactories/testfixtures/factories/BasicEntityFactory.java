package com.philvigus.dbentityfactories.testfixtures.factories;

import com.philvigus.dbentityfactories.AbstractBaseEntityFactory;
import com.philvigus.dbentityfactories.annotations.EntityFactory;
import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EntityFactory
public class BasicEntityFactory extends AbstractBaseEntityFactory<BasicEntity> {
    public BasicEntityFactory(final JpaRepository<BasicEntity, Long> repository) {
        super(BasicEntity.class, repository);
    }

    @Override
    protected Map<String, Object> getDefaultAttributes() {
        final Map<String, Object> map = new ConcurrentHashMap<>();

        map.put("myLongAttribute",
                faker.number().numberBetween(1L, 5L));

        map.put("myStringAttribute", faker.lorem().sentence());

        return map;
    }
}
