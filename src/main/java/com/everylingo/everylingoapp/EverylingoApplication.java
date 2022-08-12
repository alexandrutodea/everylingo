package com.everylingo.everylingoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

@SpringBootApplication
public class EverylingoApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(EverylingoApplication.class, args);
    }

}
