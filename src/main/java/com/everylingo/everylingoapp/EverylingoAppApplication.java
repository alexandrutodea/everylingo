package com.everylingo.everylingoapp;

import com.everylingo.everylingoapp.deepl.DeeplSupportedLanguageFetcher;
import com.everylingo.everylingoapp.deepl.DeeplTranslationProvider;
import com.everylingo.everylingoapp.model.Language;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EverylingoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverylingoAppApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(DeeplSupportedLanguageFetcher deepLSupportedLanguageFetcher,
                                        DeeplTranslationProvider deeeplTranslationProvider) {
        return args -> {
            System.out.println(deeeplTranslationProvider.translate(new Language("German", "DE"), "Hello"));
        };
    }

}
