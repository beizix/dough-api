package io.dough.api.config.security;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.useCases.auth.issueToken.adapters.jwt.IssueTokenJwtAdapter;
import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  private JwtAuthenticationFilter filter;
  private final String secret = "v-api-test-secret-key-must-be-long-enough-for-hs256";

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private MockFilterChain filterChain;

  @BeforeEach
  void setUp() {
    filter = new JwtAuthenticationFilter(secret);
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    filterChain = new MockFilterChain();
    SecurityContextHolder.clearContext();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("Scenario: 성공 - 유효한 토큰으로 인증 성공 및 권한 부여")
  void authentication_success() throws Exception {
    // Given
    UUID uuid = UUID.randomUUID();
    String email = "test@example.com";
    Role role = Role.USER;

    // 실제로 유효한 토큰 생성
    IssueTokenJwtAdapter adapter = new IssueTokenJwtAdapter(secret, 60000L, 120000L);
    String token = adapter.getAccessToken(uuid, email, "User", role);

    request.addHeader("Authorization", "Bearer " + token);

    // When
    filter.doFilterInternal(request, response, filterChain);

    // Then
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNotNull();
    assertThat(authentication.getName()).isEqualTo(uuid.toString());

    // ROLE_USER와 Role.java에서 정의된 기본 Privilege(ACCESS_USER_API)가 포함되어야 함
    assertThat(authentication.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .contains("ROLE_USER", "ACCESS_USER_API");
  }

  @Test
  @DisplayName("Scenario: 실패 - 토큰이 없는 경우 인증되지 않음")
  void authentication_fail_no_token() throws Exception {
    // When
    filter.doFilterInternal(request, response, filterChain);

    // Then
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNull();
  }
}
