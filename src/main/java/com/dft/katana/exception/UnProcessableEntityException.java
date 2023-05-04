package com.dft.katana.exception;

public class UnProcessableEntityException extends Exception{
    private static final String DEFAULT_MESSAGE = "The requested resource cannot be processed";

    public UnProcessableEntityException() {
        super(DEFAULT_MESSAGE);
    }

    public UnProcessableEntityException(String message) {
        super(message);
    }
}
