package com.everylingo.everylingoapp.exception;

public class DisabledUserAccountException extends RuntimeException {
    public DisabledUserAccountException() {
        super("User account does not exist or has been disabled");
    }
}