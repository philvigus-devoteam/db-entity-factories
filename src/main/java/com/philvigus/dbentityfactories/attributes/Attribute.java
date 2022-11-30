package com.philvigus.dbentityfactories.attributes;

public interface Attribute<T> {
    T getValue();

    String getName();
}
