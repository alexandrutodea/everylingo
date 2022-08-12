package com.everylingo.everylingoapp.deepl;

import com.everylingo.everylingoapp.exception.DeeplApiException;
import com.everylingo.everylingoapp.model.Language;
import com.everylingo.everylingoapp.model.TranslationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DeeplTranslationProvider implements TranslationProvider {

    @Autowired
    private DeeplKeyManager deepLKeyManager;
    @Autowired
    private OkHttpClient okHttpClient;
    @Autowired
    private ObjectMapper objectMapper;
    public static String DEEPL_API_URL = "https://api-free.deepl.com/v2/";

    @Override
    public String translate(Language target, String sourceText) throws IOException {

        if (target == null || sourceText == null) {
            throw new IllegalArgumentException("None of the arguments to this method may be null");
        }

        var request = createTranslationRequest(sourceText, target.getCode());
        var call = okHttpClient.newCall(request);
        var response = call.execute();

        if (response.code() != 200) {
            throw new DeeplApiException("Failed to translate source text");
        }

        var body = response.body();

        if (body == null) {
            throw new DeeplApiException("No response body returned by Deepl API");
        }

        DeeplTranslationEndpointResp deeplTranslationEndpointResp = objectMapper
                .readValue(body.string(), DeeplTranslationEndpointResp.class);

        var deepLTranslationRespList = deeplTranslationEndpointResp.getDeepLRespList();
        return deepLTranslationRespList.get(0).getText();
    }

    public Request createTranslationRequest(String sourceText, String targetLanguageCode) {
        var formBody = new FormBody.Builder()
                .add("auth_key", deepLKeyManager.getDeepLAPIKey())
                .add("text", sourceText)
                .add("target_lang", targetLanguageCode)
                .build();

        return new Request.Builder()
                .url(String.format("%s%s", DEEPL_API_URL, "translate"))
                .post(formBody)
                .build();
    }


}
