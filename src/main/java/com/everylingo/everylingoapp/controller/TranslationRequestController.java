package com.everylingo.everylingoapp.controller;

import com.everylingo.everylingoapp.model.TranslationRequest;
import com.everylingo.everylingoapp.request.CreationRequest;
import com.everylingo.everylingoapp.request.TranslationCompletionRequest;
import com.everylingo.everylingoapp.response.AutomatedTranslationResponse;
import com.everylingo.everylingoapp.service.TranslationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class TranslationRequestController {
    @Autowired
    private TranslationRequestService translationRequestService;

    @PostMapping("/requests/create")
    public void createTranslationRequest(@Valid @RequestBody CreationRequest translationCreationRequest, @AuthenticationPrincipal OAuth2User oAuth2User) throws IOException {
        translationRequestService.createTranslationRequest(translationCreationRequest, oAuth2User);
    }

    @GetMapping("/requests")
    public List<TranslationRequest> displayAllRequests(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return translationRequestService.displayAllTranslationRequests(oAuth2User);
    }

    @GetMapping("/my-requests")
    public List<TranslationRequest> displayUserRequests(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return translationRequestService.displayUserTranslationRequests(oAuth2User);
    }

    @PutMapping("/requests/{id}/complete")
    public void completeTranslationRequest(@PathVariable("id") Long id, @RequestBody @Valid TranslationCompletionRequest translationCompletionRequest, @AuthenticationPrincipal OAuth2User oAuth2User) {
        translationRequestService.completeTranslationRequest(id, translationCompletionRequest, oAuth2User);
    }

    @GetMapping("/requests/{id}/automated")
    public AutomatedTranslationResponse getAutomatedTranslation(@PathVariable("id") Long id, @AuthenticationPrincipal OAuth2User oAuth2User) throws IOException {
        return translationRequestService.getAutomatedTranslationResponse(id, oAuth2User);
    }

}