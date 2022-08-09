package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.model.Language;
import com.everylingo.everylingoapp.model.SupportedLanguageFetcher;
import com.everylingo.everylingoapp.model.TranslationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class AutomatedTranslationService {
    @Autowired
    private SupportedLanguageFetcher supportedLanguageFetcher;
    @Autowired
    private TranslationProvider translationProvider;

    public Optional<Language> getLanguageIfSupported(String languageCode) throws IOException {
        return supportedLanguageFetcher.getLanguageIfSupported(languageCode);
    }

    public String translate(Language target, String sourceText) throws IOException {
        return translationProvider.translate(target, sourceText);
    }
}
