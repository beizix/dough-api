package io.dough.api.useCases.user.maintenance.removeManager.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.maintenance.removeManager.adapters.web.model.RemoveManagerRequest;
import io.dough.api.useCases.user.maintenance.removeManager.application.RemoveManagerUseCase;
import io.dough.api.useCases.user.maintenance.removeManager.application.model.ManagerRemoved;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(RemoveManagerWebAdapter.class)
class RemoveManagerWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private RemoveManagerUseCase removeManagerUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 삭제 요청 시 UseCase가 호출된다")
  void remove_manager_success() throws Exception {
    // Given
    String managerId = UUID.randomUUID().toString();
    RemoveManagerRequest request = new RemoveManagerRequest(managerId);
    ManagerRemoved managerRemoved = new ManagerRemoved(true, LocalDateTime.now());
    given(removeManagerUseCase.operate(any())).willReturn(managerRemoved);

    // When
    mockMvc
        .perform(
            delete("/api/v1/manager/users/manager")
                .principal(() -> "admin@dough.io")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
        .andExpect(status().isOk());

    // Then
    verify(removeManagerUseCase).operate(any());
  }
}
