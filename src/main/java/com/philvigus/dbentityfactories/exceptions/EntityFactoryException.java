package com.philvigus.dbentityfactories.exceptions;

public class EntityFactoryException extends RuntimeException {
    public EntityFactoryException(final String message, final Throwable err) {
        super(message, err);
    }

    public EntityFactoryException(final String message) {
        super(message);
    }
}
