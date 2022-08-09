package com.everylingo.everylingoapp.deepl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class DeeplResp {
    @JsonProperty("detected_source_language")
    private String detectedSourceLanguage;
    @JsonProperty("text")
    private String text;

    public DeeplResp(String detectedSourceLanguage, String text) {
        this.detectedSourceLanguage = detectedSourceLanguage;
        this.text = text;
    }
}
