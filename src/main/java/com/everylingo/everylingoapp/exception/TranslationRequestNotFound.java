package com.everylingo.everylingoapp.exception;

public class TranslationRequestNotFound extends RuntimeException {
    public TranslationRequestNotFound() {
        super("Translation request does not exist");
    }
}