package io.dough.api.useCases.auth.logout.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.auth.logout.application.LogoutUseCase;
import java.security.Principal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(LogoutWebAdapter.class)
class LogoutWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private LogoutUseCase logoutUseCase;

  @Test
  @WithMockUser(username = "test@dough.io")
  @DisplayName("Scenario: 성공 - 로그아웃 요청 시 LogoutUseCase가 호출된다")
  void logout_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    Principal principal = mock(Principal.class);
    when(principal.getName()).thenReturn(userId.toString());

    // When
    mockMvc.perform(post("/api/v1/auth/logout").principal(principal)).andExpect(status().isOk());

    // Then
    verify(logoutUseCase).operate(any());
  }
}
