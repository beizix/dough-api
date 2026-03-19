package io.dough.api.useCases.auth.issueToken.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.auth.issueToken.application.port.in.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.port.in.RefreshTokenCmd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;

@WebMvcTest(RefreshTokenWebAdapter.class)
class RefreshTokenWebAdapterTest extends WebMvcTestBase {

  @Test
  @DisplayName("Scenario: 성공 - 유효한 리프레시 토큰으로 토큰 재발급")
  void refresh_token_success() throws Exception {
    // Given
    RefreshTokenRequest req = new RefreshTokenRequest("valid_refresh_token");
    AuthToken token = new AuthToken("access_token", "refresh_token");

    given(issueTokenUseCase.refreshToken(any(RefreshTokenCmd.class))).willReturn(token);

    // When
    mockMvc
        .perform(
            post("/api/v1/auth/refresh").contentType(MediaType.APPLICATION_JSON).content(json(req)))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.refreshToken").exists());
  }
}
