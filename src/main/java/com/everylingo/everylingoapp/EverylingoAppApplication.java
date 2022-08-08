package com.everylingo.everylingoapp;

import com.everylingo.everylingoapp.service.DeeplTranslator;
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
    CommandLineRunner commandLineRunner(DeeplTranslator deepLTranslator) {
        return args -> {
            deepLTranslator.fetchSupportedLanguages();
        };
    }

}
