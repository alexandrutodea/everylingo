package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.exception.UserAlreadyExistsException;
import com.everylingo.everylingoapp.model.AppUser;
import com.everylingo.everylingoapp.model.Application;
import com.everylingo.everylingoapp.repository.AppUserRepository;
import com.everylingo.everylingoapp.repository.ApplicationRepository;
import com.everylingo.everylingoapp.repository.LanguageRepository;
import com.everylingo.everylingoapp.request.SignupRequest;
import com.everylingo.everylingoapp.utils.SubExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class ApplicationService {
    @Autowired
    private SubExtractor subExtractor;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AutomatedTranslationService automatedTranslationService;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private LanguageRepository languageRepository;

    public void signup(OAuth2User oAuth2User, SignupRequest signupRequest) throws IOException {

        var externalAuthProviderId = subExtractor.extractSub(oAuth2User);
        var userAlreadyExists = appUserService.hasUserSignedUp(externalAuthProviderId);

        if (userAlreadyExists) {
            throw new UserAlreadyExistsException("You have already signed up for an account");
        }

        var appUser = new AppUser(externalAuthProviderId);
        var preferredLanguageCodes = signupRequest.getPreferredLanguageCodes();

        for (String code : preferredLanguageCodes) {
            var languageIfSupported = automatedTranslationService.getLanguageIfSupported(code);
            if (languageIfSupported.isPresent()) {
                var supportedLanguage = languageIfSupported.get();
                var dbRetrievedLanguage = languageRepository.getByCode(supportedLanguage.getCode());
                if (dbRetrievedLanguage.isPresent()) {
                    appUser.addPreferredLanguage(dbRetrievedLanguage.get());
                } else {
                    supportedLanguage.setPreferredBy(new ArrayList<>());
                    appUser.addPreferredLanguage(supportedLanguage);
                }
            }
        }

        var application = new Application(appUser);
        appUserRepository.save(appUser);
        applicationRepository.save(application);
    }
}
