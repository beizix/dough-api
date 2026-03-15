package io.dough.api.useCases.user.mgmt.updateManager.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.updateManager.adapters.web.model.UpdateManagerRequest;
import io.dough.api.useCases.user.mgmt.updateManager.application.UpdateManagerUseCase;
import io.dough.api.useCases.user.mgmt.updateManager.application.model.ManagerUpdated;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(UpdateManagerWebAdapter.class)
class UpdateManagerWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private UpdateManagerUseCase updateManagerUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 정보 수정 요청 시 UseCase가 호출된다")
  void update_manager_success() throws Exception {
    // Given
    UpdateManagerRequest request =
        new UpdateManagerRequest("manager@dough.io", "수정된이름", "newPass123!");
    ManagerUpdated managerUpdated =
        new ManagerUpdated(
            UUID.randomUUID(), "manager@dough.io", "수정된이름", Role.MANAGER, LocalDateTime.now());
    given(updateManagerUseCase.operate(any())).willReturn(managerUpdated);

    // When
    mockMvc
        .perform(
            patch("/api/v1/manager/users/manager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
        .andExpect(status().isOk());

    // Then
    verify(updateManagerUseCase).operate(any());
  }
}
