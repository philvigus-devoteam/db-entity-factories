package com.philvigus.dbentityfactories.testfixtures.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import com.philvigus.dbentityfactories.testfixtures.hibernate.repositories.BasicEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class BasicEntityRepositoryTest {
    @Autowired
    BasicEntityRepository basicEntityRepository;

    @Test
    void saveShouldSaveTheEntityToTheDatabase() {
        final BasicEntity basicEntity = new BasicEntity();

        basicEntity.setMyStringAttribute("string attrib");
        basicEntity.setMyLongAttribute(1L);

        basicEntityRepository.save(basicEntity);

        final List<BasicEntity> savedBasicEntities = basicEntityRepository.findAll();

        assertEquals(1, savedBasicEntities.size());
        assertTrue(savedBasicEntities.contains(basicEntity));
    }
}
