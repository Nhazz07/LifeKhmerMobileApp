package com.lifekhmer.publicinfo.controller;

import com.lifekhmer.common.response.ApiResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PublicContentControllerTest {

    private final PublicContentController controller = new PublicContentController();

    @Test
    void testGetHealthTipsReturnsPublicContent() {
        ApiResponse<List<Map<String, String>>> response = controller.getHealthTips();

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isNotEmpty();
        assertThat(response.data().getFirst()).containsKey("titleKm");
        assertThat(response.data().getFirst()).containsKey("descriptionKm");
    }

    @Test
    void testGetAppInfoReturnsSupportedLanguages() {
        ApiResponse<Map<String, Object>> response = controller.getAppInfo();

        assertThat(response.success()).isTrue();
        assertThat(response.data().get("name")).isEqualTo("LifeKhmer");
        Object languages = response.data().get("supportedLanguages");
        assertThat(languages).isInstanceOf(List.class);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) languages;
        assertThat(list).contains("km", "en");
    }
}
