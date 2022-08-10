package com.everylingo.everylingoapp.test.mothers;

import com.everylingo.everylingoapp.model.AppUser;
import com.everylingo.everylingoapp.model.Language;
import com.everylingo.everylingoapp.model.TranslationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        var appUser = Mother.appUser();
        return new TranslationRequest(123L, null, "Hello", null, german, romanian);
    }

    public static List<Language> get3Languages() {
        var languageList = new ArrayList<Language>();
        languageList.add(new Language("Romanian", "RO"));
        languageList.add(new Language("German", "DE"));
        languageList.add(new Language("French", "FR"));
        return languageList;
    }


}
