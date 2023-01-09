package com.philvigus.dbentityfactories.exceptions;

/**
 * The Entity factory exception class.
 */
public class EntityFactoryException extends RuntimeException {
    /**
     * Instantiates a new Entity factory exception.
     *
     * @param message the exception message
     * @param err     the exception being wrapped
     */
    public EntityFactoryException(final String message, final Throwable err) {
        super(message, err);
    }

    /**
     * Instantiates a new Entity factory exception.
     *
     * @param message the exception message
     */
    public EntityFactoryException(final String message) {
        super(message);
    }
}
