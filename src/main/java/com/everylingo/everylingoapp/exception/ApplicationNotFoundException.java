package com.everylingo.everylingoapp.exception;

public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException() {
        super("Application not found");
    }
}
