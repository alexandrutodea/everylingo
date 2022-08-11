package com.everylingo.everylingoapp.repository;

import com.everylingo.everylingoapp.model.AppUser;
import com.everylingo.everylingoapp.model.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByAuthProviderId(String authProviderId);

    Optional<AppUser> findByAuthProviderIdAndRole(String authProviderId, AppUserRole role);
}
