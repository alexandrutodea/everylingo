package com.everylingo.everylingoapp.deepl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DeeplTranslationEndpointResp {
    @JsonProperty("translations")
    private List<DeeplResp> deepLRespList;

    public DeeplTranslationEndpointResp() {
        this.deepLRespList = new ArrayList<>();
    }
}
