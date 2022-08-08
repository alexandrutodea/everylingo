package com.everylingo.everylingoapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class DeeplKeyManager {

    private final String deepLAPIKey;

    public DeeplKeyManager(@Autowired Environment environment) {
        this.deepLAPIKey = environment.getProperty("DEEPL_API_KEY");
    }

    public String getDeepLAPIKey() {
        return deepLAPIKey;
    }
}
