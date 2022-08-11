package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.exception.UserAlreadyExistsException;
import com.everylingo.everylingoapp.model.AppUser;
import com.everylingo.everylingoapp.model.Application;
import com.everylingo.everylingoapp.model.Language;
import com.everylingo.everylingoapp.repository.AppUserRepository;
import com.everylingo.everylingoapp.repository.ApplicationRepository;
import com.everylingo.everylingoapp.repository.LanguageRepository;
import com.everylingo.everylingoapp.request.SignupRequest;
import com.everylingo.everylingoapp.test.mothers.Mother;
import com.everylingo.everylingoapp.utils.SubExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private SubExtractor subExtractor;

    @Captor
    private ArgumentCaptor<String> languageCodeCaptor;

    @Mock
    private AutomatedTranslationService automatedTranslationService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserService appUserService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    @DisplayName("Should throw exception if user alreadt exists")
    void shouldThrowExceptionIfUserAlreadtExists() {
        //Arrange
        var oauth2user = mock(OAuth2User.class);
        var authProviderId = Mother.authProviderId();
        when(subExtractor.extractSub(oauth2user)).thenReturn(authProviderId);
        var signUpRequest = mock(SignupRequest.class);
        when(appUserService.hasUserSignedUp(authProviderId)).thenReturn(true);
        //Act + Assert
        assertThatThrownBy(() -> applicationService.signup(oauth2user, signUpRequest))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("You have already signed up for an account");

    }

    @Test
    @DisplayName("Should save a new user if user has not already signed up")
    void shouldSaveANewUserIfUserHasNotAlreadySignedUp() throws IOException {
        //Arrange
        var oauth2user = mock(OAuth2User.class);
        var authProviderId = Mother.authProviderId();
        when(subExtractor.extractSub(oauth2user)).thenReturn(authProviderId);
        var signUpRequest = mock(SignupRequest.class);
        when(appUserService.hasUserSignedUp(authProviderId)).thenReturn(false);
        //Act
        applicationService.signup(oauth2user, signUpRequest);
        //Assert
        verify(appUserRepository).save(any(AppUser.class));
    }

    @Test
    @DisplayName("Should save a new application if user has not already signed up")
    void shouldSaveANewApplicationIfUserHasNotAlreadySignedUp() throws IOException {
        //Arrange
        var oauth2user = mock(OAuth2User.class);
        var authProviderId = Mother.authProviderId();
        when(subExtractor.extractSub(oauth2user)).thenReturn(authProviderId);
        var signUpRequest = mock(SignupRequest.class);
        when(appUserService.hasUserSignedUp(authProviderId)).thenReturn(false);
        //Act
        applicationService.signup(oauth2user, signUpRequest);
        //Assert
        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    @DisplayName("Should call automatedTranslationService if user has not already signed up")
    void shouldCallAutomatedTranslationServiceIfUserHasNotAlreadySignedUp() throws IOException {
        //Arrange
        var oauth2user = mock(OAuth2User.class);
        var authProviderId = Mother.authProviderId();
        when(subExtractor.extractSub(oauth2user)).thenReturn(authProviderId);
        var signUpRequest = mock(SignupRequest.class);
        when(appUserService.hasUserSignedUp(authProviderId)).thenReturn(false);
        var languageCodes = new ArrayList<String>();
        languageCodes.add("RO");
        when(signUpRequest.getPreferredLanguageCodes()).thenReturn(languageCodes);
        //Act
        applicationService.signup(oauth2user, signUpRequest);
        //Assert
        verify(automatedTranslationService).getLanguageIfSupported(languageCodeCaptor.capture());
        assertThat(languageCodeCaptor.getValue()).isEqualTo(languageCodes.get(0));
    }

    @Test
    @DisplayName("Should call language repository if language is supported")
    void shouldCallLanguageRepositoryIfLanguageIsSupported() throws IOException {
        //Arrange
        var oauth2user = mock(OAuth2User.class);
        var authProviderId = Mother.authProviderId();
        when(subExtractor.extractSub(oauth2user)).thenReturn(authProviderId);
        var signUpRequest = mock(SignupRequest.class);
        when(appUserService.hasUserSignedUp(authProviderId)).thenReturn(false);
        var languageCodes = new ArrayList<String>();
        var languageCode = "RO";
        languageCodes.add(languageCode);
        when(signUpRequest.getPreferredLanguageCodes()).thenReturn(languageCodes);
        when(automatedTranslationService.getLanguageIfSupported(languageCode))
                .thenReturn(Optional.of(new Language("Romanian", "RO")));
        //Act
        applicationService.signup(oauth2user, signUpRequest);
        //Assert
        verify(languageRepository).getByCode(languageCodeCaptor.capture());
        assertThat(languageCodeCaptor.getValue()).isEqualTo(languageCode);
    }
}