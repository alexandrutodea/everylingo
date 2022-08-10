package com.everylingo.everylingoapp.model;

import com.everylingo.everylingoapp.repo.LanguageRepository;
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
class LanguageTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    @DisplayName("Should not be able to save two languages with the same name")
    void shouldNotBeAbleToSaveTwoLanguagesWithTheSameName() {
        var romanian = Mother.romanianLanguage();
        var duplicate = new Language("Romanian", "DE");
        testEntityManager.persist(romanian);
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(duplicate))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Should not able to save two languages with the same code")
    void shouldNotAbleToSaveTwoLanguagesWithTheSameCode() {
        var romanian = Mother.romanianLanguage();
        var duplicate = new Language("German", "RO");
        testEntityManager.persist(romanian);
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(duplicate))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Should be able to save language")
    void shouldBeAbleToSaveLanguage() {
        var romanian = Mother.romanianLanguage();
        var savedLanguage = languageRepository.save(romanian);
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
        var romanian = new Language(null, "RO");
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(romanian))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Should not be able to save language with null code")
    void shouldNotBeAbleToSaveLanguageWithNullCode() {
        var romanian = new Language("Romanian", null);
        assertThatThrownBy(() -> testEntityManager
                .persistAndFlush(romanian))
                .isInstanceOf(PersistenceException.class);
    }


}