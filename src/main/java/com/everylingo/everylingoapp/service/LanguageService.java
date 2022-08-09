package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.model.Language;
import com.everylingo.everylingoapp.repo.TranslationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LanguageService {

    @Autowired
    TranslationRequestRepository translationRequestRepository;

    public void doLanguagesGetSavedTwice() {
        var romanian = new Language("Romanian", "RO");
        var romanianSecond = new Language("Romanian", "RO");
        translationRequestRepository.save(romanian);
        translationRequestRepository.save(romanianSecond);
    }

}
