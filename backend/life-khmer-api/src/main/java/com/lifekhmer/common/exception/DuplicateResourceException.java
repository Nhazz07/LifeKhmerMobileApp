package com.lifekhmer.common.exception;

/** A specialised conflict for a resource that must be unique. */
public class DuplicateResourceException extends ConflictException {
    public DuplicateResourceException(String message) { super(message); }
    public DuplicateResourceException(String message, Throwable cause) { super(message, cause); }
}
