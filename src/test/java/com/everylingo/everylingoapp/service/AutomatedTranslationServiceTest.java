package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.model.Language;
import com.everylingo.everylingoapp.model.SupportedLanguageFetcher;
import com.everylingo.everylingoapp.model.TranslationProvider;
import com.everylingo.everylingoapp.test.mothers.DataMother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Automated Translation Service Test")
class AutomatedTranslationServiceTest {

    @Mock
    private SupportedLanguageFetcher supportedLanguageFetcher;
    @Mock
    private TranslationProvider translationProvider;
    @Captor
    private ArgumentCaptor<String> languageCodeCaptor;
    @Captor
    private ArgumentCaptor<Language> languageCaptor;
    @Captor
    private ArgumentCaptor<String> sourceTextCaptor;
    @InjectMocks
    private AutomatedTranslationService automatedTranslationService;

    @Test
    @DisplayName("getLanguageIfSupported should call supported language fetcher")
    void getLanguageIfSupportedShouldCall() throws IOException {
        //Arrange
        var language = DataMother.language();
        //Act
        automatedTranslationService.getLanguageIfSupported(language.getCode());
        //Assert
        verify(supportedLanguageFetcher).getLanguageIfSupported(languageCodeCaptor.capture());
        assertThat(languageCodeCaptor.getValue()).isEqualTo(language.getCode());
        verifyNoMoreInteractions(supportedLanguageFetcher);
        verifyNoInteractions(translationProvider);
    }

    @Test
    @DisplayName("translate method should call translation provider")
    void translateShouldCallTranslationProvider() throws IOException {
        //Arrange
        var language = DataMother.language();
        var text = "Hello";
        //Act
        automatedTranslationService.translate(language, text);
        //Assert
        verify(translationProvider).translate(languageCaptor.capture(), sourceTextCaptor.capture());
        assertThat(languageCaptor.getValue()).isEqualTo(language);
        assertThat(sourceTextCaptor.getValue()).isEqualTo(text);
        verifyNoInteractions(supportedLanguageFetcher);
        verifyNoMoreInteractions(translationProvider);
    }

}