package com.jwt.impl.exceptions;

public class NonexistentTaskException extends RuntimeException {
    public NonexistentTaskException(String message) {
        super(message);
    }
}
