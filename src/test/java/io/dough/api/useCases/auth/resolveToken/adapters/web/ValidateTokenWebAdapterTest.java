package io.dough.api.useCases.auth.resolveToken.adapters.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.auth.resolveToken.adapters.web.model.ValidateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;

@WebMvcTest(ValidateTokenWebAdapter.class)
class ValidateTokenWebAdapterTest extends WebMvcTestBase {

  @Test
  @DisplayName("Scenario: 성공 - 토큰 유효성 검증")
  void validate_token_success() throws Exception {
    // Given
    ValidateRequest req = new ValidateRequest("valid_access_token");
    given(resolveTokenUseCase.validateToken(req.token())).willReturn(true);

    // When
    mockMvc
        .perform(
            post("/api/v1/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(req)))
        // Then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.valid").value(true));
  }
}
