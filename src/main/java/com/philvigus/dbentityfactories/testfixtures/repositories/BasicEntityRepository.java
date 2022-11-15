package com.philvigus.dbentityfactories.testfixtures.repositories;

import com.philvigus.dbentityfactories.testfixtures.entities.BasicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicEntityRepository extends JpaRepository<BasicEntity, Long> {
}
