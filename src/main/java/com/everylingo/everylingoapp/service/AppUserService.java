package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public boolean hasUserSignedUp(String authProviderId) {

        if (authProviderId == null) {
            return false;
        }

        var byAuthProviderId = appUserRepository.findByAuthProviderId(authProviderId);
        return byAuthProviderId.isPresent();
    }

}
