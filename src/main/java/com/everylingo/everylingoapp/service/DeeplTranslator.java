package com.everylingo.everylingoapp.service;

import com.everylingo.everylingoapp.exception.DeeplApiException;
import com.everylingo.everylingoapp.model.Language;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeeplTranslator implements AutomatedTranslator {


    private List<Language> supportedLanguages;
    private DeeplKeyManager deepLKeyManager;
    private OkHttpClient okHttpClient;
    private ObjectMapper objectMapper;
    public static String DEEPL_API_URL = "https://api-free.deepl.com/v2/";

    public DeeplTranslator(
            @Autowired DeeplKeyManager deepLKeyManager,
            @Autowired OkHttpClient okHttpClient,
            @Autowired ObjectMapper objectMapper) {
        this.deepLKeyManager = deepLKeyManager;
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
        this.supportedLanguages = new ArrayList<>();
    }

    @Override
    public String translate(String source, Language target) {
        return null;
    }

    @Override
    public Optional<Language> getLanguageIfSupported(String languageName) {
        return Optional.empty();
    }

    public void fetchSupportedLanguages() throws IOException {

        if (supportedLanguages.size() != 0) {
            return;
        }

        var request = createSupportedLanguagesRequest();
        var call = okHttpClient.newCall(request);
        var response = call.execute();

        if (response.code() != 200) {
            throw new DeeplApiException("Failed to fetch supported languages");
        }

        var body = response.body();

        if (body == null) {
            throw new DeeplApiException("No response body returned by Deepl API");
        }

        var responseBodyString = body.string();
        System.out.println(responseBodyString);
        this.supportedLanguages = objectMapper.readValue(responseBodyString, new TypeReference<>() {
        });
        System.out.println(this.supportedLanguages);
    }

    void addSupportedLanguage(Language language) {
        this.supportedLanguages.add(language);
    }

    void removeSupportedLanguage(Language language) {
        this.supportedLanguages.remove(language);
    }

    int getNumberOfSupportedLanguages() {
        return this.supportedLanguages.size();
    }

    void clearSupportedLanguagesList() {
        this.supportedLanguages.clear();
    }

    public Request createSupportedLanguagesRequest() {
        var formBody = new FormBody.Builder()
                .add("auth_key", deepLKeyManager.getDeepLAPIKey())
                .add("type", "target")
                .build();

        return new Request.Builder()
                .url(String.format("%s%s", DEEPL_API_URL, "languages"))
                .post(formBody)
                .build();
    }

    public List<Language> getSupportedLanguages() {
        return this.supportedLanguages;
    }
}
