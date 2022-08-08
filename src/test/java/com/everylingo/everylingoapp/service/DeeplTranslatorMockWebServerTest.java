package com.everylingo.everylingoapp.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DeeplTranslatorMockWebServerTest {

    private static MockWebServer mockWebServer;
    @Autowired
    private DeeplTranslator deeplTranslator;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8080);
    }

    @Test
    @DisplayName("An exception should get thrown if DeepL Translate API response is not 200")
    void anExceptionShouldGetThrownIfDeepLTranslateApiResponseIsNot200() throws IOException, InterruptedException {
        //Arrange
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).setBody("{\"error_code\": null, \"error_message\": null}"));
        //Act
        deeplTranslator.fetchSupportedLanguages();
        //Assert
        var recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("POST");
        assertThat(recordedRequest.getPath()).isEqualTo("languages");
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }
}
