package com.everylingo.everylingoapp.repository;

import com.everylingo.everylingoapp.model.TranslationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRequestRepository extends JpaRepository<TranslationRequest, Long> {
}
