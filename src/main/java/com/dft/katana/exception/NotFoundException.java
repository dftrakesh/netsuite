package com.dft.katana.exception;

public class NotFoundException extends Exception{
    private static final String DEFAULT_MESSAGE = "The requested resource could not be found";

    public NotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
