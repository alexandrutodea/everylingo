package com.everylingo.everylingoapp.utils;

import com.everylingo.everylingoapp.test.mothers.Mother;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SubExtractorTest {

    private SubExtractor subExtractor = new SubExtractor();

    @Test
    @DisplayName("Should return sub attribute if given valid OAuth2User")
    void shouldReturnSubAttributeIfGivenValidOAuth2User() {
        var oAuth2User = Mother.auth2User();
        assertThat(subExtractor.extractSub(oAuth2User)).isEqualTo(oAuth2User.getAttributes().get("sub"));
    }

}