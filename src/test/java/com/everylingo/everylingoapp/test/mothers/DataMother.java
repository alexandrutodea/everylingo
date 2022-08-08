package com.everylingo.everylingoapp.test.mothers;

import com.everylingo.everylingoapp.model.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataMother {

    private DataMother() {
        //it makes no sense to instantiate this class
    }

    public static String apiKey() {
        return UUID.randomUUID().toString();
    }

    public static Language language() {
        return new Language("Romanian", "RO");
    }

    public static List<Language> get3Languages() {
        var languageList = new ArrayList<Language>();
        languageList.add(new Language("Romanian", "RO"));
        languageList.add(new Language("German", "DE"));
        languageList.add(new Language("French", "FR"));
        return languageList;
    }


}
