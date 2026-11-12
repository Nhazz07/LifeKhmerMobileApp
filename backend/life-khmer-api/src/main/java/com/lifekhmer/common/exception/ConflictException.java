package com.lifekhmer.common.exception;

/** Indicates that a request conflicts with the current resource state. */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) { super(message); }
    public ConflictException(String message, Throwable cause) { super(message, cause); }
}
