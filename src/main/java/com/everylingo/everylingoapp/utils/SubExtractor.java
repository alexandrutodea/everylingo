package com.everylingo.everylingoapp.utils;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class SubExtractor {

    public String extractSub(OAuth2User oAuth2User) {
        return oAuth2User.getAttribute("sub");
    }

}
