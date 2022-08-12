package com.everylingo.everylingoapp.response;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class AutomatedTranslationResponse {
    private String automatedTranslation;

    public AutomatedTranslationResponse(String automatedTranslation) {
        this.automatedTranslation = automatedTranslation;
    }
}
