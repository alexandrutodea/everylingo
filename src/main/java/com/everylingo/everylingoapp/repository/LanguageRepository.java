package com.everylingo.everylingoapp.repository;

import com.everylingo.everylingoapp.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findByCode(String code);
}
