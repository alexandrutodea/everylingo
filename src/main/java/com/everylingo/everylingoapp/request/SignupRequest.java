package com.everylingo.everylingoapp.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 50)
    private String message;
    @NotEmpty
    private List<String> preferredLanguageCodes;
}
