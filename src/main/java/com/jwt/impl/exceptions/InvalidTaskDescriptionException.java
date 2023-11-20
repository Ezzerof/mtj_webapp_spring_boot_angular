package com.jwt.impl.exceptions;

public class InvalidTaskDescriptionException extends RuntimeException {
    public InvalidTaskDescriptionException(String message) {
        super(message);
    }
}
