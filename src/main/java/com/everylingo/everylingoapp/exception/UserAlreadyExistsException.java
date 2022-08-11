package com.everylingo.everylingoapp.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("You have already signed up for an account");
    }

}
