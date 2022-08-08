package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.model.Language;

import java.util.Optional;

public interface AutomatedTranslator {
    String translate(String source, Language target);

    Optional<Language> getLanguageIfSupported(String languageName);
}
