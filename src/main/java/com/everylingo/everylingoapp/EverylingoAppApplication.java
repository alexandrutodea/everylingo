package com.everylingo.everylingoapp;

import com.everylingo.everylingoapp.deepl.DeeplSupportedLanguageFetcher;
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
    CommandLineRunner commandLineRunner(DeeplSupportedLanguageFetcher deepLSupportedLanguageFetcher) {
        return args -> {
            deepLSupportedLanguageFetcher.fetchSupportedLanguages();
        };
    }

}
