package io.dough.api.useCases.user.maintenance.createManager.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.createManager.adapters.web.model.CreateManagerRequest;
import io.dough.api.useCases.user.maintenance.createManager.application.CreateManagerUseCase;
import io.dough.api.useCases.user.maintenance.createManager.domain.ManagerCreated;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(CreateManagerWebAdapter.class)
class CreateManagerWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private CreateManagerUseCase createManagerUseCase;

  @Test
  @WithMockUser(roles = "MANAGER")
  @DisplayName("Scenario: 성공 - 매니저 생성 요청 시 UseCase가 호출된다")
  void create_manager_success() throws Exception {
    // Given
    CreateManagerRequest request = new CreateManagerRequest("manager@dough.io", "새매니저", "pass1234!");
    ManagerCreated managerCreated =
        new ManagerCreated(
            UUID.randomUUID(), "manager@dough.io", "새매니저", Role.MANAGER, LocalDateTime.now());
    given(createManagerUseCase.operate(any())).willReturn(managerCreated);

    // When
    mockMvc
        .perform(
            post("/api/v1/manager/users/manager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
        .andExpect(status().isOk());

    // Then
    verify(createManagerUseCase).operate(any());
  }
}
