package com.everylingo.everylingoapp.repository;

import com.everylingo.everylingoapp.model.TranslationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslationRequestRepository extends JpaRepository<TranslationRequest, Long> {
    Optional<TranslationRequest> findByRequestedById(Long id);
}
