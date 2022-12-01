package com.philvigus.dbentityfactories.exceptions;

public class DuplicateUniqueAttributeValueException extends EntityFactoryException {
    public DuplicateUniqueAttributeValueException(final String message) {
        super(message);
    }
}
