package com.everylingo.everylingoapp.model;

import java.io.IOException;

public interface TranslationProvider {
    String translate(Language target, String sourceText) throws IOException;
}
