package com.everylingo.everylingoapp.model;

import com.everylingo.everylingoapp.test.mothers.Mother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppUserTest {

    private final AppUser appUser = Mother.appUser();

    @Test
    @DisplayName("Adding preferred language automatically adds user to language user list")
    void addingPreferredLanguageSetsLanguageAppUser() {
        var language = Mother.germanLanguage();
        appUser.addPreferredLanguage(language);
        assertThat(appUser.getPreferredLanguages()).contains(language);
        assertThat(language.getPreferredBy()).contains(appUser);
    }

    @Test
    @DisplayName("Removing language automatically removes user from language user list")
    void removingLanguageAutomaticallyRemovesUserFromLanguageUserList() {
        var language = Mother.germanLanguage();
        appUser.addPreferredLanguage(language);
        appUser.removePreferredLanguage(language);
        assertThat(appUser.getPreferredLanguages().size()).isEqualTo(0);
        assertThat(language.getPreferredBy().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Removing translation request sets translation request user to null")
    void removingTranslationRequestSetsTranslationRequestUserToNull() {
        var request = Mother.translationRequest();
        appUser.addTranslationRequest(request);
        appUser.removeTranslationRequest(request);
        assertThat(request.getRequestedBy()).isNull();
    }

    @Test
    @DisplayName("Adding translation request automatically sets request user")
    void addingTranslationRequestAutomaticallySetsRequestUser() {
        var request = Mother.translationRequest();
        appUser.addTranslationRequest(request);
        assertThat(request.getRequestedBy()).isEqualTo(appUser);
    }


}