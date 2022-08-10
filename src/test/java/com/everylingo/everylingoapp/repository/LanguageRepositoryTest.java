package com.everylingo.everylingoapp.repository;

import com.everylingo.everylingoapp.model.Language;
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
class LanguageRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    @DisplayName("Should not be able to save two languages with the same name")
    void shouldNotBeAbleToSaveTwoLanguagesWithTheSameName() {
        //Arrange
        var romanian = Mother.romanianLanguage();
        var duplicate = new Language("Romanian", "DE");
        testEntityManager.persist(romanian);
        //Act + Assert
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(duplicate))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Should not able to save two languages with the same code")
    void shouldNotAbleToSaveTwoLanguagesWithTheSameCode() {
        //Arrange
        var romanian = Mother.romanianLanguage();
        var duplicate = new Language("German", "RO");
        //Act
        testEntityManager.persist(romanian);
        //Assert
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(duplicate))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Should be able to save language")
    void shouldBeAbleToSaveLanguage() {
        //Arrange
        var romanian = Mother.romanianLanguage();
        //Act
        var savedLanguage = languageRepository.save(romanian);
        //Assert
        var savedLanguageId = savedLanguage.getId();
        var retrievedLanguage = languageRepository.findById(savedLanguageId);
        assertThat(retrievedLanguage).isPresent();
        assertThat(retrievedLanguage.get())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(romanian);
    }

    @Test
    @DisplayName("Should not be able to save language with null name")
    void shouldNotBeAbleToSaveLanguageWithNullName() {
        //Arrange
        var romanian = new Language(null, "RO");
        //Act + Assert
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(romanian))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Should not be able to save language with null code")
    void shouldNotBeAbleToSaveLanguageWithNullCode() {
        //Arrange
        var romanian = new Language("Romanian", null);
        //Act + Assert
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(romanian))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("findByCode method should return a list of the languages with the given code")
    void findByCodeMethodShouldReturnAListOfTheLanguagesWithTheGivenCode() {
        //Arrange
        var language = Mother.romanianLanguage();
        languageRepository.save(language);
        //Act
        var byCode = languageRepository.findByCode(language.getCode());
        //Assert
        assertThat(byCode.size()).isEqualTo(1);
        assertThat(byCode.get(0))
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(language);
    }


}