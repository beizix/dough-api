package io.dough.api.useCases.user.mgmt.registerManager.adapters.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.registerManager.adapters.in.web.RegisterManagerRequest;
import io.dough.api.useCases.user.mgmt.registerManager.application.port.in.RegisterManagerUseCase;
import io.dough.api.useCases.user.mgmt.registerManager.application.port.in.ManagerRegistered;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(RegisterManagerWebAdapter.class)
class RegisterManagerWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private RegisterManagerUseCase registerManagerUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 생성 요청 시 UseCase가 호출된다")
  void create_manager_success() throws Exception {
    // Given
    RegisterManagerRequest request =
        new RegisterManagerRequest("manager@dough.io", "새매니저", "pass1234!");
    ManagerRegistered managerRegistered =
        new ManagerRegistered(
            UUID.randomUUID(), "manager@dough.io", "새매니저", Role.MANAGER, LocalDateTime.now());
    given(registerManagerUseCase.operate(any())).willReturn(managerRegistered);

    // When
    mockMvc
        .perform(
            post("/api/v1/manager/users/manager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
        .andExpect(status().isOk());

    // Then
    verify(registerManagerUseCase).operate(any());
  }
}
