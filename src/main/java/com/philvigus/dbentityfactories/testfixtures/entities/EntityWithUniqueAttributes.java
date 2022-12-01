package com.philvigus.dbentityfactories.testfixtures.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class EntityWithUniqueAttributes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uniqueString;

    @Column(unique = true)
    private Long uniqueLong;

    private String repeatableString;

    private Long repeatableLong;
}
