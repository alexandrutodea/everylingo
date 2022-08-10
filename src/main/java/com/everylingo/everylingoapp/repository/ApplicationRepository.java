package com.everylingo.everylingoapp.repository;

import com.everylingo.everylingoapp.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
