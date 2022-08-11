package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.exception.ApplicationNotFoundException;
import com.everylingo.everylingoapp.exception.NotAnAdminException;
import com.everylingo.everylingoapp.exception.UserAlreadyExistsException;
import com.everylingo.everylingoapp.model.AppUser;
import com.everylingo.everylingoapp.model.AppUserRole;
import com.everylingo.everylingoapp.model.Application;
import com.everylingo.everylingoapp.model.Status;
import com.everylingo.everylingoapp.repository.AppUserRepository;
import com.everylingo.everylingoapp.repository.ApplicationRepository;
import com.everylingo.everylingoapp.repository.LanguageRepository;
import com.everylingo.everylingoapp.request.SignupRequest;
import com.everylingo.everylingoapp.utils.SubExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {
    @Autowired
    private SubExtractor subExtractor;

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
        var byAuthProviderId = appUserRepository.findByAuthProviderId(externalAuthProviderId);

        if (byAuthProviderId.isPresent()) {
            throw new UserAlreadyExistsException();
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

        var application = new Application(appUser, signupRequest.getMessage());
        appUserRepository.save(appUser);
        applicationRepository.save(application);
    }

    public List<Application> getAllApplications(OAuth2User oAuth2User) {
        checkAdmin(oAuth2User);
        return applicationRepository.findAll();
    }

    @Transactional
    @Modifying
    public void updateApplicationStatus(Long id, boolean enableUser, Status status, OAuth2User oAuth2User) {
        checkAdmin(oAuth2User);

        var byId = applicationRepository.findById(id);

        if (byId.isEmpty()) {
            throw new ApplicationNotFoundException();
        }

        var application = byId.get();
        application.setStatus(status);
        var appUser = application.getAppUser();
        appUser.setEnabled(enableUser);
        appUserRepository.save(appUser);
        applicationRepository.save(application);
    }

    public void checkAdmin(OAuth2User oAuth2User) {
        var sub = subExtractor.extractSub(oAuth2User);

        if (appUserRepository.findByAuthProviderIdAndRole(sub, AppUserRole.ADMIN).isEmpty()) {
            throw new NotAnAdminException();
        }
    }
}
