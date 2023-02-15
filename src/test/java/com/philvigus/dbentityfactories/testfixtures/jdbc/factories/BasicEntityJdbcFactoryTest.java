package com.philvigus.dbentityfactories.testfixtures.jdbc.factories;

import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BasicEntityJdbcFactoryTest {
    @Autowired
    BasicEntityJdbcFactory basicEntityJdbcFactory;

    @Test
    void persistShouldCreateAndSaveABasicEntityToTheDatabase() {
        BasicEntity savedBasicEntity = basicEntityJdbcFactory.persist();

        assertNotNull(savedBasicEntity.getId());
        assertNotNull(savedBasicEntity.getMyStringAttribute());
        assertNotNull(savedBasicEntity.getMyLongAttribute());
    }
}
