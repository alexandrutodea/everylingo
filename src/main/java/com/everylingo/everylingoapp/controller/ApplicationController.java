package com.everylingo.everylingoapp.controller;

import com.everylingo.everylingoapp.model.Application;
import com.everylingo.everylingoapp.model.Status;
import com.everylingo.everylingoapp.request.SignupRequest;
import com.everylingo.everylingoapp.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;


    @PostMapping(path = "/sign-up")
    public void signup(@Valid @RequestBody SignupRequest signupRequest, @AuthenticationPrincipal OAuth2User principal) throws IOException {
        applicationService.signup(principal, signupRequest);
    }

    @PostMapping(path = "/whatever")
    public void whatever(@RequestBody SignupRequest signupRequest) {

    }

    @GetMapping(path = "/applications")
    public List<Application> getApplications(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return applicationService.getAllApplications(oAuth2User);
    }

    @PutMapping(path = "/applications/{id}/approve")
    public void approveApplication(@PathVariable Long id, @AuthenticationPrincipal OAuth2User oAuth2User) {
        applicationService.updateApplicationStatus(id, true, Status.APPROVED, oAuth2User);
    }

    @PutMapping(path = "/applications/{id}/deny")
    public void denyApplication(@PathVariable Long id, @AuthenticationPrincipal OAuth2User oAuth2User) {
        applicationService.updateApplicationStatus(id, false, Status.DENIED, oAuth2User);
    }

    @DeleteMapping(path = "applications/{id}")
    public void removeApplication(@PathVariable Long id, @AuthenticationPrincipal OAuth2User oAuth2User) {
        applicationService.removeApplication(id, oAuth2User);
    }

}
