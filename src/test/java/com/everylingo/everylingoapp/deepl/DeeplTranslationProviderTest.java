package com.everylingo.everylingoapp.deepl;

import com.everylingo.everylingoapp.exception.DeeplApiException;
import com.everylingo.everylingoapp.test.mothers.DataMother;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeepLTranslationProvider Test")
class DeeplTranslationProviderTest {

    @Mock
    private DeeplKeyManager deepLKeyManager;
    @Mock
    private OkHttpClient okHttpClient;
    @Mock
    private ObjectMapper objectMapper;
    @Captor
    private ArgumentCaptor<String> rawBodyCaptor;
    @InjectMocks
    private DeeplTranslationProvider deeplTranslationProvider = new DeeplTranslationProvider();

    @Test
    @DisplayName("DeepL Key Manager should be called when sending a request to the DeepL translation endpoint")
    void deepLKeyManagerShouldBeCalledWhenSendingARequestToTheDeepLTranslationEndpoints() throws IOException {
        //Arrange
        var language = DataMother.language();
        var text = "Hello";
        var apiKey = DataMother.apiKey();
        when(deepLKeyManager.getDeepLAPIKey()).thenReturn(apiKey);
        //Act
        deeplTranslationProvider.createTranslationRequest(text, language.getCode());
        //Assert
        verify(deepLKeyManager).getDeepLAPIKey();
        verifyNoMoreInteractions(deepLKeyManager);
        verifyNoInteractions(objectMapper);
        verifyNoInteractions(okHttpClient);
    }

    @Test
    @DisplayName("Exception should get thrown if provided target language is null")
    void exceptionShouldGetThrownIfProvidedTargetLanguageIsNull() {
        //Act + Assert
        assertThatThrownBy(() -> deeplTranslationProvider.translate(null, "Hello"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("None of the arguments to this method may be null");
    }

    @Test
    @DisplayName("Exception should get thrown if provided source text is null")
    void exceptionShouldGetThrownIfProvidedSourceTextIsNull() {
        //Arrange
        var language = DataMother.language();
        //Act + Assert
        assertThatThrownBy(() -> deeplTranslationProvider.translate(language, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("None of the arguments to this method may be null");
    }

    @Test
    @DisplayName("Exception should get thrown if DeepL API response is not 200")
    void exceptionShouldGetThrownIfDeepLApiResponseIsNot200() throws IOException {
        //Arrange
        var language = DataMother.language();
        var apiKey = DataMother.apiKey();
        when(deepLKeyManager.getDeepLAPIKey()).thenReturn(apiKey);
        var call = mock(Call.class);
        var response = mock(Response.class);
        when(call.execute()).thenReturn(response);
        when(okHttpClient.newCall(any(Request.class))).thenReturn(call);
        when(response.code()).thenReturn(400);
        //Act + Assert
        assertThatThrownBy(() -> deeplTranslationProvider.translate(language, "Hello"))
                .isInstanceOf(DeeplApiException.class)
                .hasMessageContaining("Failed to translate source text");
    }

    @Test
    @DisplayName("Exception should get thrown if DeepL API response body is null")
    void exceptionShouldGetThrownIfDeepLApiResponseBodyIsNull() throws IOException {
        //Arrange
        var language = DataMother.language();
        var apiKey = DataMother.apiKey();
        when(deepLKeyManager.getDeepLAPIKey()).thenReturn(apiKey);
        var call = mock(Call.class);
        when(okHttpClient.newCall(any(Request.class))).thenReturn(call);
        var response = mock(Response.class);
        when(call.execute()).thenReturn(response);
        when(response.code()).thenReturn(200);
        when(response.body()).thenReturn(null);
        //Act + Assert
        assertThatThrownBy(() -> deeplTranslationProvider.translate(language, "Hello"))
                .isInstanceOf(DeeplApiException.class)
                .hasMessageContaining("No response body returned by Deepl API");
    }

    @Test
    @DisplayName("Should be capable of properly parsing valid DeepL API response")
    void shouldBeCapableOfProperlyParsingValidDeepLApiResponse() throws IOException {
        //Arrange
        var apiKey = DataMother.apiKey();
        when(deepLKeyManager.getDeepLAPIKey()).thenReturn(apiKey);
        var language = DataMother.language();
        var call = mock(Call.class);
        when(okHttpClient.newCall(any(Request.class))).thenReturn(call);
        var response = mock(Response.class);
        when(call.execute()).thenReturn(response);
        when(response.code()).thenReturn(200);
        var body = mock(ResponseBody.class);
        when(response.body()).thenReturn(body);
        var rawResponseBody = """
                {"translations":[{"detected_source_language":"EN","text":"Hallo"}]}
                """;
        var deeplTranslationEndpointResp = new DeeplTranslationEndpointResp();
        deeplTranslationEndpointResp.getDeepLRespList().add(new DeeplResp("EN", "Hallo"));
        when(body.string()).thenReturn(rawResponseBody);
        when(objectMapper.readValue(rawResponseBody, DeeplTranslationEndpointResp.class))
                .thenReturn(deeplTranslationEndpointResp);
        //Act
        deeplTranslationProvider.translate(language, "Hello");
        //Assert
        verify(objectMapper).readValue(rawBodyCaptor.capture(), any(Class.class));
        assertThat(rawBodyCaptor.getValue()).isEqualTo(rawResponseBody);
        verifyNoMoreInteractions(deepLKeyManager);
        verifyNoMoreInteractions(okHttpClient);
        verifyNoMoreInteractions(objectMapper);
    }

}