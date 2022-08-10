package com.everylingo.everylingoapp.repository;

import com.everylingo.everylingoapp.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Query("SELECT l FROM Language l WHERE l.code = ?1")
    List<Language> findByCode(String code);

    Optional<Language> getByCode(String code);
}
