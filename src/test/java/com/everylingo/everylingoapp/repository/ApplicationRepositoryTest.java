package com.everylingo.everylingoapp.repository;

import com.everylingo.everylingoapp.model.Application;
import com.everylingo.everylingoapp.test.mothers.Mother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    @DisplayName("Should be able to save application")
    void shouldBeAbleToSaveApplication() {
        //Arrange
        var user = Mother.appUser();
        var message = Mother.message();
        appUserRepository.save(user);
        var application = new Application(user, message);
        //Act
        var savedApplication = applicationRepository.save(application);
        //Assert
        var savedId = savedApplication.getId();
        var byId = applicationRepository.findById(savedId);
        assertThat(byId).isPresent();
        assertThat(byId.get())
                .usingRecursiveComparison()
                .ignoringFields("appUser")
                .isEqualTo(application);
    }

}