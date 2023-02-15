package com.philvigus.dbentityfactories.testfixtures.jdbc.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BasicEntityJdbcRepositoryTest {
    @Autowired
    BasicEntityJdbcRepository basicEntityJdbcRepository;

    @Test
    void saveShouldSaveTheEntityToTheDatabase() {
        final BasicEntity basicEntity = new BasicEntity();

        basicEntity.setMyStringAttribute("string attrib");
        basicEntity.setMyLongAttribute(1L);

        basicEntityJdbcRepository.save(basicEntity);

        final List<BasicEntity> savedBasicEntities = (List<BasicEntity>) basicEntityJdbcRepository.findAll();

        assertEquals(1, savedBasicEntities.size());
    }
}
