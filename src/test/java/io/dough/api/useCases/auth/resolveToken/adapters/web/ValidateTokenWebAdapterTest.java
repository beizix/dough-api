package io.dough.api.useCases.auth.resolveToken.adapters.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.auth.resolveToken.adapters.web.model.ValidateRequest;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.shared.domain.auth.Token;
import io.jsonwebtoken.Jwts;
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
    UUID uuid = UUID.randomUUID();
    String email = "test@example.com";
    Role role = Role.USER;

    // 실제로 유효한 토큰 생성
    String token =
        Jwts.builder()
            .subject(uuid.toString())
            .claim("email", email)
            .claim("type", Token.access.name())
            .claim("role", role.getAuthority())
            .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
            .compact();
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
