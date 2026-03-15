package io.dough.api.useCases.user.mgmt.deleteManager.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.mgmt.deleteManager.adapters.web.model.DeleteManagerRequest;
import io.dough.api.useCases.user.mgmt.deleteManager.application.DeleteManagerUseCase;
import io.dough.api.useCases.user.mgmt.deleteManager.application.model.ManagerDeleted;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(DeleteManagerWebAdapter.class)
class DeleteManagerWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private DeleteManagerUseCase deleteManagerUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 삭제 요청 시 UseCase가 호출된다")
  void delete_manager_success() throws Exception {
    // Given
    String managerId = UUID.randomUUID().toString();
    DeleteManagerRequest request = new DeleteManagerRequest(managerId);
    ManagerDeleted managerDeleted = new ManagerDeleted(true, LocalDateTime.now());
    given(deleteManagerUseCase.operate(any())).willReturn(managerDeleted);

    // When
    mockMvc
        .perform(
            delete("/api/v1/manager/users/manager")
                .principal(() -> "admin@dough.io")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
        .andExpect(status().isOk());

    // Then
    verify(deleteManagerUseCase).operate(any());
  }
}
