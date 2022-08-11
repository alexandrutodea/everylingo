package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.repository.AppUserRepository;
import com.everylingo.everylingoapp.test.mothers.Mother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @InjectMocks
    private AppUserService appUserService;

    @Test
    @DisplayName("hasAlreadySignedUp test should return false if passed null")
    void hasAlreadySignedUpTestShouldReturnEmptyOptionalIfPassedNull() {
        var res = appUserService.hasUserSignedUp(null);
        assertThat(res).isFalse();
    }

    @Test
    @DisplayName("hasAlreadySignedUp should return true if given a valid AuthProviderId")
    void hasAlreadySignedUpShouldReturnTrueIfGivenAValidAuthProviderId() {
        //Arrange
        var authProviderId = Mother.authProviderId();
        var appUser = Mother.appUser();
        when(appUserRepository.findByAuthProviderId(authProviderId)).thenReturn(Optional.of(appUser));
        //Act
        var res = appUserService.hasUserSignedUp(authProviderId);
        //Assert
        assertThat(res).isTrue();
    }

}