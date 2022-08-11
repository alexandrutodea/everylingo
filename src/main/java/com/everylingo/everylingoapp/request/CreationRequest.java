package com.everylingo.everylingoapp.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CreationRequest {
    @Size(min = 10)
    private String sourceText;
    @Size(min = 2)
    private String sourceLanguageCode;
    @Size(min = 2)
    private String targetLanguageCode;
}