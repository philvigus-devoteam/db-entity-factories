package com.philvigus.dbentityfactories.examples.repositories;

import com.philvigus.dbentityfactories.examples.entities.NewUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewUserRepository extends JpaRepository<NewUser, Long> {
}
