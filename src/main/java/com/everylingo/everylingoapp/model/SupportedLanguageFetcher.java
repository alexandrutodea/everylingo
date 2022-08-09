package com.everylingo.everylingoapp.model;

import java.io.IOException;
import java.util.Optional;

public interface SupportedLanguageFetcher {
    public Optional<Language> getLanguageIfSupported(String languageCode) throws IOException;
}
