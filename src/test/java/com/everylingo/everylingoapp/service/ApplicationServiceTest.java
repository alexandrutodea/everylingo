package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.exception.ApplicationNotFoundException;
import com.everylingo.everylingoapp.exception.NotAnAdminException;
import com.everylingo.everylingoapp.exception.UserAlreadyExistsException;
import com.everylingo.everylingoapp.model.*;
import com.everylingo.everylingoapp.repository.AppUserRepository;
import com.everylingo.everylingoapp.repository.ApplicationRepository;
import com.everylingo.everylingoapp.repository.LanguageRepository;
import com.everylingo.everylingoapp.request.SignupRequest;
import com.everylingo.everylingoapp.test.mothers.Mother;
import com.everylingo.everylingoapp.utils.SubExtractor;
import lombok.extern.java.Log;
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
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private SubExtractor subExtractor;

    @Captor
    private ArgumentCaptor<String> languageCodeCaptor;

    @Captor
    private ArgumentCaptor<Long> idCaptor;

    @Mock
    private AutomatedTranslationService automatedTranslationService;

    @Mock
    private AppUserRepository appUserRepository;

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
        when(appUserRepository.findByAuthProviderId(authProviderId)).thenReturn(Optional.of(mock(AppUser.class)));
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
        when(appUserRepository.findByAuthProviderId(authProviderId)).thenReturn(Optional.empty());
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
        when(appUserRepository.findByAuthProviderId(authProviderId)).thenReturn(Optional.empty());
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
        when(appUserRepository.findByAuthProviderId(authProviderId)).thenReturn(Optional.empty());
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
        when(appUserRepository.findByAuthProviderId(authProviderId)).thenReturn(Optional.empty());
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

    @Test
    @DisplayName("getAllApplications throws exception if user is not an admin")
    void getAllApplicationsThrowsExceptionIfUserIsNotAnAdmin() {
        //Arrange
        var user = mock(OAuth2User.class);
        var authProviderId = Mother.authProviderId();
        when(subExtractor.extractSub(user)).thenReturn(authProviderId);
        when(appUserRepository.findByAuthProviderIdAndRole(authProviderId, AppUserRole.ADMIN)).thenReturn(Optional.empty());
        //Act + Assert
        assertThatThrownBy(() -> applicationService.getAllApplications(user))
                .isInstanceOf(NotAnAdminException.class)
                .hasMessageContaining("No admin privileges");
    }

    @Test
    @DisplayName("getAllApplications retrieves all applications from the database if user is an admin")
    void getAllApplicationsRetrievesAllApplicationsFromTheDatabaseIfUserIsAnAdmin() {
        //Arrange
        var user = mock(OAuth2User.class);
        var authProviderId = Mother.authProviderId();
        when(subExtractor.extractSub(user)).thenReturn(authProviderId);
        when(appUserRepository.findByAuthProviderIdAndRole(authProviderId, AppUserRole.ADMIN))
                .thenReturn(Optional.of(mock(AppUser.class)));
        //Act
        applicationService.getAllApplications(user);
        //Assert
        verify(applicationRepository).findAll();
    }

    @Test
    @DisplayName("Users with no admin rights cannot update the status of an application")
    void usersWithNoAdminRightsCannotUpdateTheStatusOfAnApplication() {
        //Arrange
        var user = mock(OAuth2User.class);
        var authProviderId = Mother.authProviderId();
        when(subExtractor.extractSub(user)).thenReturn(authProviderId);
        when(appUserRepository.findByAuthProviderIdAndRole(authProviderId, AppUserRole.ADMIN))
                .thenReturn(Optional.empty());
        //Act + Assert
        assertThatThrownBy(() -> applicationService.updateApplicationStatus(123L, true, Status.APPROVED, user))
                .isInstanceOf(NotAnAdminException.class)
                .hasMessageContaining("No admin privileges");
    }

    @Test
    @DisplayName("An exception should get thrown if application cannot be found")
    void anExceptionShouldGetThrownIfApplicationCannotBeFound() {
        //Arrange
        var random = new Random();
        var id = random.nextLong();
        var user = mock(OAuth2User.class);
        when(applicationRepository.findById(id)).thenReturn(Optional.empty());
        when(appUserRepository.findByAuthProviderIdAndRole(any(), any()))
                .thenReturn(Optional.of(mock(AppUser.class)));
        //Act + Assert
        assertThatThrownBy(() -> applicationService.updateApplicationStatus(id, true, Status.APPROVED, user))
                .isInstanceOf(ApplicationNotFoundException.class)
                .hasMessageContaining("Application not found");
    }

    @Test
    @DisplayName("User and application should be saved if application can be found")
    void userAndApplicationShouldBeSavedIfApplicationCanBeFound() {
        //Arrange
        var random = new Random();
        var id = random.nextLong();
        var user = mock(OAuth2User.class);
        var application = mock(Application.class);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        var appUser = Mother.appUser();
        appUser.setRole(AppUserRole.ADMIN);
        when(application.getAppUser()).thenReturn(appUser);
        when(appUserRepository.findByAuthProviderIdAndRole(any(), any()))
                .thenReturn(Optional.of(appUser));
        //Act
        applicationService.updateApplicationStatus(id, true, Status.APPROVED, user);
        //Assert
        verify(appUserRepository).save(any());
        verify(applicationRepository).save(any());
    }

    @Test
    @DisplayName("Deleting an application should search the application by id")
    void deletingAUserShouldSearchTheUserById() {
        //Arrange
        var user = mock(OAuth2User.class);
        var id = 123L;
        when(appUserRepository.findByAuthProviderIdAndRole(any(), any())).thenReturn(Optional.of(mock(AppUser.class)));
        //Act
        applicationService.removeApplication(id, user);
        //Assert
        verify(applicationRepository).findById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(id);
    }

    @Test
    @DisplayName("Deleting an application should call the delete method on the application repository")
    void deletingAnApplicationShouldCallTheDeleteMethodOnTheApplicationRepository() {
        //Arrange
        var user = mock(OAuth2User.class);
        var id = 123L;
        when(appUserRepository.findByAuthProviderIdAndRole(any(), any())).thenReturn(Optional.of(mock(AppUser.class)));
        when(applicationRepository.findById(id)).thenReturn(Optional.of(mock(Application.class)));
        //Act
        applicationService.removeApplication(id, user);
        //Assert
        verify(applicationRepository).delete(any(Application.class));
    }


}