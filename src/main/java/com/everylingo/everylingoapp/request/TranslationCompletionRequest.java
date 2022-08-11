package com.everylingo.everylingoapp.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class TranslationCompletionRequest {
    @Size(min = 10)
    private String translation;
}