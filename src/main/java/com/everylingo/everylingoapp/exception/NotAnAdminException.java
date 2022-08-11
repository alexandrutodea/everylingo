package com.everylingo.everylingoapp.exception;

public class NotAnAdminException extends RuntimeException {

    public NotAnAdminException() {
        super("No admin privileges");
    }

}
