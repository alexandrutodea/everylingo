package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.exception.DisabledUserAccountException;
import com.everylingo.everylingoapp.exception.TranslationRequestNotFound;
import com.everylingo.everylingoapp.model.AppUser;
import com.everylingo.everylingoapp.model.Language;
import com.everylingo.everylingoapp.model.TranslationRequest;
import com.everylingo.everylingoapp.repository.AppUserRepository;
import com.everylingo.everylingoapp.repository.LanguageRepository;
import com.everylingo.everylingoapp.repository.TranslationRequestRepository;
import com.everylingo.everylingoapp.request.CreationRequest;
import com.everylingo.everylingoapp.request.TranslationCompletionRequest;
import com.everylingo.everylingoapp.utils.SubExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class TranslationRequestService {

    @Autowired
    private SubExtractor subExtractor;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TranslationRequestRepository translationRequestRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private AutomatedTranslationService automatedTranslationService;

    public void createTranslationRequest(CreationRequest translationCreationRequest, OAuth2User oAuth2User) throws IOException {

        var appUser = getUserIfEnabledAndActive(oAuth2User);
        var sourceLanguageCode = translationCreationRequest.getSourceLanguageCode();
        var targetLanguageCode = translationCreationRequest.getTargetLanguageCode();

        Language sourceLanguage = null;
        Language targetLanguage = null;

        var retrievedSourceLanguage = automatedTranslationService.
                getLanguageIfSupported(sourceLanguageCode);

        var retrievedTargetLanguage = automatedTranslationService.
                getLanguageIfSupported(targetLanguageCode);

        if (retrievedSourceLanguage.isPresent()) {
            var dbLanguage = languageRepository.getByCode(sourceLanguageCode);
            sourceLanguage = dbLanguage.orElseGet(retrievedSourceLanguage::get);
        }

        if (retrievedTargetLanguage.isPresent()) {
            var dbLanguage = languageRepository.getByCode(targetLanguageCode);
            targetLanguage = dbLanguage.orElseGet(retrievedSourceLanguage::get);
        }

        if (sourceLanguage == null) {
            sourceLanguage = new Language(null, sourceLanguageCode);
        }

        if (targetLanguage == null) {
            targetLanguage = new Language(null, targetLanguageCode);
        }

        var sourceText = translationCreationRequest.getSourceText();
        var translationRequest = new TranslationRequest(appUser, sourceText, null, sourceLanguage, targetLanguage);
        translationRequestRepository.save(translationRequest);
        appUser.addTranslationRequest(translationRequest);
        appUserRepository.save(appUser);
    }

    public List<TranslationRequest> displayAllTranslationRequests(OAuth2User oAuth2User) {
        getUserIfEnabledAndActive(oAuth2User);
        return translationRequestRepository.findAll();
    }

    public List<TranslationRequest> displayUserTranslationRequests(OAuth2User oAuth2User) {
        var appUser = getUserIfEnabledAndActive(oAuth2User);
        return appUser.getTranslationRequests();
    }

    @Modifying
    @Transactional
    public void completeTranslationRequest(Long translationId, TranslationCompletionRequest translationCompletionRequest, OAuth2User oAuth2User) {

        getUserIfEnabledAndActive(oAuth2User);

        var byId = translationRequestRepository.findByRequestedById(translationId);

        if (byId.isEmpty()) {
            throw new TranslationRequestNotFound();
        }

        var translationRequest = byId.get();
        translationRequest.setTranslation(translationCompletionRequest.getTranslation());
        translationRequestRepository.save(translationRequest);
    }


    public AppUser getUserIfEnabledAndActive(OAuth2User oAuth2User) {
        var authProviderId = subExtractor.extractSub(oAuth2User);

        var appUser = appUserRepository.findAppUserByAuthProviderIdAndEnabled(authProviderId, true);

        if (appUser.isEmpty()) {
            throw new DisabledUserAccountException();
        }

        return appUser.get();
    }
}