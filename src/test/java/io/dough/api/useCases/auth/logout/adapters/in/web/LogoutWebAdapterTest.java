package io.dough.api.useCases.auth.logout.adapters.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.auth.logout.application.port.in.LogoutUseCase;
import java.security.Principal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(LogoutWebAdapter.class)
class LogoutWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private LogoutUseCase logoutUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 로그아웃 요청 시 LogoutUseCase가 호출된다")
  void logout_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();

    // When
    mockMvc
        .perform(post("/api/v1/auth/logout").principal((Principal) () -> userId.toString()))
        .andExpect(status().isOk());

    // Then
    verify(logoutUseCase).operate(any());
  }
}
