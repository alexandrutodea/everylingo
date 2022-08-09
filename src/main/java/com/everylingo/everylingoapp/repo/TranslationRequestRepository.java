package com.everylingo.everylingoapp.repo;

import com.everylingo.everylingoapp.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRequestRepository extends JpaRepository<Language, Long> {
}
