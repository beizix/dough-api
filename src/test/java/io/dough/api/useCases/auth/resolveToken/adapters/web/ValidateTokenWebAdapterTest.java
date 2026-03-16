package io.dough.api.useCases.auth.resolveToken.adapters.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.auth.issueToken.domain.TokenIssuer;
import io.dough.api.useCases.auth.resolveToken.adapters.web.model.ValidateRequest;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
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
    String secret = "test-secret-key-for-vision-api-unit-testing-purposes-only";
    // 실제로 유효한 토큰 생성
    TokenIssuer issuer = new TokenIssuer(
        Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)),
        UUID.randomUUID(), "test@example.com", "User", Role.USER, new Date(), 60000L, 120000L
    );
    String token = issuer.getAccessToken();
    ValidateRequest req = new ValidateRequest(token);

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
