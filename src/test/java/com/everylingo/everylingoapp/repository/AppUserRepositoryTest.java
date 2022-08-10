package com.everylingo.everylingoapp.repository;

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
class AppUserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TranslationRequestRepository translationRequestRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    @DisplayName("Should be able to save user")
    void shouldBeAbleToSaveUser() {
        //Arrange
        var appUser = Mother.appUser();
        //Act
        var savedAppUser = appUserRepository.save(appUser);
        //Assert
        var retrievedAppUser = appUserRepository.findById(savedAppUser.getId());
        assertThat(retrievedAppUser).isPresent();
        assertThat(retrievedAppUser.get())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(appUser);

    }

    @Test
    @DisplayName("Should not be able to save two users with the same authProviderId")
    void shouldNotBeAbleToSaveTwoUsersWithTheSameAuthProviderId() {
        //Arrange
        var firstUser = Mother.appUser();
        var secondUser = Mother.appUser();
        secondUser.setAuthProviderId(firstUser.getAuthProviderId());
        testEntityManager.persist(firstUser);
        //Act+ Assert
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(secondUser))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Should not be able to save users with a null authProviderId")
    void shouldNotBeAbleToSaveUsersWithANullAuthProviderId() {
        //Arrange
        var appUser = Mother.appUser();
        appUser.setAuthProviderId(null);
        //Act + Assert
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(appUser))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Saving a user should also save their translation requests")
    void savingAUserShouldAlsoSaveTheirTranslationRequests() {
        //Arrange
        var user = Mother.appUser();
        var translationRequest = Mother.translationRequest();
        user.addTranslationRequest(translationRequest);
        var savedUser = appUserRepository.save(user);
        //Act
        var retrievedRequest = translationRequestRepository.findByRequestedById(savedUser.getId());
        //Assert
        assertThat(retrievedRequest).isPresent();
        assertThat(retrievedRequest.get().getRequestedBy())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedUser);
    }

    @Test
    @DisplayName("Adding the same preferred language to two different users should not save it to the database twice")
    void addingTheSamePreferredLanguageToTwoDifferentUsersShouldNotSaveItToTheDatabaseTwice() {
        //Arrange
        var language = Mother.romanianLanguage();
        var savedLanguage = languageRepository.save(language);
        var firstUser = Mother.appUser();
        firstUser.addPreferredLanguage(savedLanguage);
        appUserRepository.save(firstUser);
        //Act
        var savedLanguageId = savedLanguage.getId();
        var retrievedLanguage = languageRepository.findById(savedLanguageId);
        assertThat(retrievedLanguage).isPresent();
        var secondUser = Mother.appUser();
        secondUser.addPreferredLanguage(retrievedLanguage.get());
        appUserRepository.save(secondUser);
        //Assert
        assertThat(languageRepository.findByCode(language.getCode()).size()).isEqualTo(1);
    }

}