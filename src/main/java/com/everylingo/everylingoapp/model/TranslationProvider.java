package com.everylingo.everylingoapp.model;

public interface TranslationProvider {
    String translate(Language target, String sourceText);
}
