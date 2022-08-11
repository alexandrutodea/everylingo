package com.everylingo.everylingoapp.controller;

import com.everylingo.everylingoapp.request.SignupRequest;
import com.everylingo.everylingoapp.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping(path = "/sign-up")
    public void signup(@Valid @RequestBody SignupRequest signupRequest, @AuthenticationPrincipal OAuth2User principal) throws IOException {
        applicationService.signup(principal, signupRequest);
    }
}
