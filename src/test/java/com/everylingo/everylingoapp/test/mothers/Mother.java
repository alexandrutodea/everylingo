package com.everylingo.everylingoapp.test.mothers;

import com.everylingo.everylingoapp.model.AppUser;
import com.everylingo.everylingoapp.model.Language;
import com.everylingo.everylingoapp.model.TranslationRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class Mother {

    private Mother() {
        //it makes no sense to instantiate this class
    }

    public static String apiKey() {
        return UUID.randomUUID().toString();
    }

    public static Language romanianLanguage() {
        return new Language("Romanian", "RO");
    }

    public static Language germanLanguage() {
        return new Language("German", "DE");
    }

    public static AppUser appUser() {
        return new AppUser(UUID.randomUUID().toString());
    }

    public static TranslationRequest translationRequest() {
        var german = Mother.germanLanguage();
        var romanian = Mother.romanianLanguage();
        return new TranslationRequest(null, "Hello", null, german, romanian);
    }

    public static List<Language> get3Languages() {
        var languageList = new ArrayList<Language>();
        languageList.add(new Language("Romanian", "RO"));
        languageList.add(new Language("German", "DE"));
        languageList.add(new Language("French", "FR"));
        return languageList;
    }

    public static String authProviderId() {
        return "google-oauth2|111851965579678264193";
    }

    public static OAuth2User auth2User() {
        return new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                return Map.of("sub", Mother.authProviderId());
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return new ArrayList<>();
            }

            @Override
            public String getName() {
                return "Jane Doe";
            }
        };
    }

    public static String message() {
        return "Hello! My name is Alexandru and I would like to help with translations on your website. I am highly motivated person.";
    }


}
