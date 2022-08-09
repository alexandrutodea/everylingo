package com.everylingo.everylingoapp.deepl;

import com.everylingo.everylingoapp.exception.DeeplApiException;
import com.everylingo.everylingoapp.test.mothers.DataMother;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("DeeplSupportedLanguageFetcher Tests")
@ExtendWith(MockitoExtension.class)
class DeeplSupportedLanguageFetcherTest {
    @Mock
    private DeeplKeyManager deepLKeyManager;
    @Mock
    private OkHttpClient okHttpClient;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private DeeplSupportedLanguageFetcher deepLSupportedLanguageFetcher = new DeeplSupportedLanguageFetcher();

    @Test
    @DisplayName("There should be no attempt to fetch supported languages if they have already been fetched")
    void thereShouldBeNoAttemptToFetchSupportedLanguagesIfTheyHaveAlreadyBeenFetched() throws IOException {
        //Arrange (list gets instantiated in object constructor)
        var language = DataMother.language();
        this.deepLSupportedLanguageFetcher.addSupportedLanguage(language);
        //Act
        deepLSupportedLanguageFetcher.fetchSupportedLanguages();
        //Assert
        verifyNoInteractions(deepLKeyManager);
        verifyNoInteractions(okHttpClient);
    }

    @Test
    @DisplayName("DeeplAPIKeyManager gets called when creating supported languages request")
    void deeplApiKeyManagerGetsCalledWhenCreatingSupportedLanguagesRequest() {
        //Arrange
        var apiKey = DataMother.apiKey();
        when(deepLKeyManager.getDeepLAPIKey()).thenReturn(apiKey);
        //Act
        var request = deepLSupportedLanguageFetcher.createSupportedLanguagesRequest();
        //Assert
        verify(deepLKeyManager).getDeepLAPIKey();
        verifyNoMoreInteractions(deepLKeyManager);
        verifyNoInteractions(okHttpClient);
    }

    @Test
    @DisplayName("Proper DeepL API endpoint gets called when creating supported languages request")
    void properDeepLApiUrlGetsCalledWhenCreatingSupportedLanguagesRequest() throws URISyntaxException {
        //Arrange
        var apiKey = DataMother.apiKey();
        when(deepLKeyManager.getDeepLAPIKey()).thenReturn(apiKey);
        var expectedUrl = String.format("%s%s", DeeplSupportedLanguageFetcher.DEEPL_API_URL, "languages");
        //Act
        var request = deepLSupportedLanguageFetcher.createSupportedLanguagesRequest();
        //Assert
        assertThat(request.url().toString()).isEqualTo(expectedUrl);
        verifyNoMoreInteractions(deepLKeyManager);
        verifyNoInteractions(okHttpClient);
    }

    @Test
    @DisplayName("addSupportedLanguage method should add language to the list of supported languages")
    void addSupportedLanguageMethodShouldAddLanguageToTheListOfSupportedLanguages() {
        //Arrange
        var language = DataMother.language();
        //Act + Assert
        assertThat(deepLSupportedLanguageFetcher.getNumberOfSupportedLanguages()).isEqualTo(0);
        deepLSupportedLanguageFetcher.addSupportedLanguage(language);
        assertThat(deepLSupportedLanguageFetcher.getNumberOfSupportedLanguages()).isEqualTo(1);
        verifyNoInteractions(deepLKeyManager);
        verifyNoInteractions(okHttpClient);
    }

    @Test
    @DisplayName("removeSupportedLanguage method should remove a supported Language from this list of supported languages")
    void removeSupportedLanguageMethodShouldRemoveASupportedLanguageFromThisListOfSupportedLanguages() {
        //Arrange
        var language = DataMother.language();
        deepLSupportedLanguageFetcher.addSupportedLanguage(language);
        //Act + Assert
        assertThat(deepLSupportedLanguageFetcher.getNumberOfSupportedLanguages()).isEqualTo(1);
        deepLSupportedLanguageFetcher.removeSupportedLanguage(language);
        assertThat(deepLSupportedLanguageFetcher.getNumberOfSupportedLanguages()).isEqualTo(0);
        verifyNoInteractions(deepLKeyManager);
        verifyNoInteractions(okHttpClient);
    }

    @Test
    @DisplayName("clearSupportedLanguagesList should clear the list of supported languages")
    void clearSupportedLanguagesListShouldClearTheListOfSupportedLanguages() {
        //Arrange
        var languages = DataMother.get3Languages();
        //Act + Assert
        languages
                .forEach(language -> deepLSupportedLanguageFetcher.addSupportedLanguage(language));
        assertThat(deepLSupportedLanguageFetcher.getNumberOfSupportedLanguages()).isEqualTo(3);
        deepLSupportedLanguageFetcher.clearSupportedLanguagesList();
        assertThat(deepLSupportedLanguageFetcher.getNumberOfSupportedLanguages()).isEqualTo(0);
        verifyNoInteractions(okHttpClient);
        verifyNoInteractions(deepLKeyManager);
    }

    @Test
    @DisplayName("Exception should get thrown if DeepL API call does not return 200")
    void exceptionShouldGetThrownIfDeepLApiCallDoesNotReturn200() throws IOException {
        //Arrange
        var call = mock(Call.class);
        when(okHttpClient.newCall(any()))
                .thenReturn(call);
        var response = mock(Response.class);
        when(call.execute()).thenReturn(response);
        when(response.code()).thenReturn(400);
        var apiKey = DataMother.apiKey();
        when(deepLKeyManager.getDeepLAPIKey()).thenReturn(apiKey);
        //Act + Assert
        assertThatThrownBy(() -> deepLSupportedLanguageFetcher
                .fetchSupportedLanguages())
                .isInstanceOf(DeeplApiException.class)
                .hasMessageContaining("Failed to fetch supported languages");
    }

    @Test
    @DisplayName("Exception should get thrown if response body is null")
    void exceptionShouldGetThrownIfResponseBodyIsNull() throws IOException {
        //Arrange
        var call = mock(Call.class);
        when(okHttpClient.newCall(any()))
                .thenReturn(call);
        var response = mock(Response.class);
        when(call.execute()).thenReturn(response);
        when(response.code()).thenReturn(200);
        when(response.body()).thenReturn(null);
        var apiKey = DataMother.apiKey();
        when(deepLKeyManager.getDeepLAPIKey()).thenReturn(apiKey);
        //Act + Assert
        assertThatThrownBy(() -> deepLSupportedLanguageFetcher
                .fetchSupportedLanguages())
                .isInstanceOf(DeeplApiException.class)
                .hasMessageContaining("No response body returned by Deepl API");
    }

    @Test
    @DisplayName("Should be capable of successfully converting JSON Array response into a language List")
    void shouldBeCapableOfSuccessfullyConvertingJsonArrayResponseIntoALanguageList() throws IOException {
        //Arrange
        var call = mock(Call.class);
        when(okHttpClient.newCall(any()))
                .thenReturn(call);
        var response = mock(Response.class);
        when(call.execute()).thenReturn(response);
        when(response.code()).thenReturn(200);
        var responseBody = mock(ResponseBody.class);
        when(response.body()).thenReturn(responseBody);
        when(responseBody.string()).thenReturn("""
                [{"language":"RO","name":"Romanian","supports_formality":false},
                {"language":"DE","name":"German","supports_formality":false},
                {"language":"FR","name":"French","supports_formality":false}
                """);
        var apiKey = DataMother.apiKey();
        var expectedLanguages = DataMother.get3Languages();
        when(deepLKeyManager.getDeepLAPIKey()).thenReturn(apiKey);
        when(objectMapper.readValue(any(String.class), any(TypeReference.class))).thenReturn(expectedLanguages);
        //Act
        deepLSupportedLanguageFetcher.fetchSupportedLanguages();
        //Assert
        assertThat(deepLSupportedLanguageFetcher.getSupportedLanguages())
                .containsExactlyInAnyOrderElementsOf(expectedLanguages);
        verifyNoMoreInteractions(deepLKeyManager);
        verifyNoMoreInteractions(objectMapper);
        verifyNoMoreInteractions(okHttpClient);
    }

    @Test
    @DisplayName("getSupportedLanguages should return list of currently supported languages")
    void getSupportedLanguagesShouldReturnListOfCurrentlySupportedLanguages() throws IOException {
        //Arrange
        var languages = DataMother.get3Languages();
        languages
                .forEach(language -> deepLSupportedLanguageFetcher.addSupportedLanguage(language));
        //Act
        deepLSupportedLanguageFetcher.fetchSupportedLanguages();
        //Assert
        assertThat(deepLSupportedLanguageFetcher
                .getSupportedLanguages())
                .containsExactlyInAnyOrderElementsOf(languages);
        verifyNoInteractions(deepLKeyManager);
        verifyNoInteractions(okHttpClient);
        verifyNoInteractions(objectMapper);
    }

    @Test
    @DisplayName("getLanguageIfSupported should return supported language if it exists")
    void getLanguageIfSupportedMethodShouldAttemptToFetchLanguagesIfTheyHaveNotAlreadyBeenFetched() throws IOException {
        //Arrange
        var expectedLanguage = DataMother.language();
        deepLSupportedLanguageFetcher.addSupportedLanguage(expectedLanguage);
        //Act
        var supportedLanguage = deepLSupportedLanguageFetcher.getLanguageIfSupported(expectedLanguage.getCode());
        //Assert
        assertThat(supportedLanguage).isNotEmpty();
        assertThat(supportedLanguage.get()).isEqualTo(expectedLanguage);
        verifyNoInteractions(deepLKeyManager);
        verifyNoInteractions(okHttpClient);
        verifyNoInteractions(objectMapper);
    }

    @Test
    @DisplayName("getLanguageIfSupported should fetch languages if no languages have previously fetched")
    void getLanguageIfSupportedShouldFetchLanguagesAndReturnLanguageIfNoLanguagesHaveBeenAdded() throws IOException {
        var spy = spy(deepLSupportedLanguageFetcher);
        doNothing().when(spy).fetchSupportedLanguages();
        spy.getLanguageIfSupported("DE");
        verify(spy).fetchSupportedLanguages();
    }

    @AfterEach
    void tearDown() {
        deepLSupportedLanguageFetcher.clearSupportedLanguagesList();
    }
}