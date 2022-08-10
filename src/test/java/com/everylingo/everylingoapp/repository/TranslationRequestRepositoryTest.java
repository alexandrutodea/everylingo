package com.everylingo.everylingoapp.repository;

import com.everylingo.everylingoapp.model.TranslationRequest;
import com.everylingo.everylingoapp.test.mothers.Mother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class TranslationRequestRepositoryTest {

    @Autowired
    private TranslationRequestRepository translationRequestRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("Should not able to save a translation request with a null source language")
    void shouldNotAbleToSaveATranslationRequestWithANullSourceLanguage() {
        //Arrange
        var requestedBy = Mother.appUser();
        testEntityManager.persist(requestedBy);
        var request = new TranslationRequest(requestedBy,
                "Hello",
                null,
                null,
                Mother.germanLanguage());
        //Act + Assert
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(request))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Should not be able to save a translation request with a null target language")
    void shouldNotBeAbleToSaveATranslationRequestWithANullTargetLanguage() {
        //Arrange
        var requestedBy = Mother.appUser();
        testEntityManager.persist(requestedBy);
        var request = new TranslationRequest(requestedBy,
                "Hello",
                null,
                Mother.germanLanguage(),
                null);
        //Act + Assert
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(request))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Should be able to save a translation request")
    void shouldBeAbleToSaveATranslationRequest() {
        //Arrange
        var appUser = Mother.appUser();
        var savedAppUser = appUserRepository.save(appUser);
        var sourceLanguage = Mother.germanLanguage();
        var targetLanguage = Mother.romanianLanguage();
        var savedSourceLanguage = languageRepository.save(sourceLanguage);
        var savedTargetLanguage = languageRepository.save(targetLanguage);
        var sourceText = "Hello";
        var translationRequest = new TranslationRequest(savedAppUser, sourceText, null, savedSourceLanguage, savedTargetLanguage);
        //Act
        var savedTranslationRequest = translationRequestRepository.save(translationRequest);
        var id = savedTranslationRequest.getId();
        var retrievedTranslationRequest = translationRequestRepository.findById(id);
        //Assert
        assertThat(retrievedTranslationRequest).isPresent();
        assertThat(retrievedTranslationRequest.get().getRequestedBy())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(appUser);
        assertThat(retrievedTranslationRequest.get().getSourceLanguage())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(sourceLanguage);
        assertThat(retrievedTranslationRequest.get().getTargetLanguage())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(targetLanguage);
        assertThat(retrievedTranslationRequest.get().getSourceText())
                .isEqualTo(sourceText);
    }

    @Test
    @DisplayName("findByRequestById method should return the linked appUser")
    void findByRequestByIdMethodShouldReturnTheLinkedAppUser() {
        //Arrange
        var appUser = Mother.appUser();
        var savedAppUser = appUserRepository.save(appUser);
        var translationRequest = Mother.translationRequest();
        translationRequest.setRequestedBy(appUser);
        var id = savedAppUser.getId();
        //Act
        translationRequestRepository.save(translationRequest);
        //Assert
        var retrievedRequest = translationRequestRepository.findByRequestedById(id);
        assertThat(retrievedRequest).isPresent();
        assertThat(retrievedRequest.get().getRequestedBy())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedAppUser);
    }

}