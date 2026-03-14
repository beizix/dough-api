package io.dough.api.useCases.user.maintenance.getUsers.adapters.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.maintenance.getUsers.application.GetUsersUseCase;
import io.dough.api.useCases.user.maintenance.getUsers.application.model.GetUsers;
import io.dough.api.useCases.user.maintenance.getUsers.application.model.GetUsersCmd;
import io.dough.api.useCases.user.maintenance.getUsers.application.model.PageInfo;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(GetUsersWebAdapter.class)
class GetUsersWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private GetUsersUseCase getUsersUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 목록 조회 시 필터와 페이징 정보가 UseCase로 전달된다")
  void get_users_success() throws Exception {
    // Given
    String email = "test@example.com";
    String displayName = "테스터";
    String role = "USER";

    given(getUsersUseCase.operate(any(GetUsersCmd.class)))
        .willReturn(new GetUsers(List.of(), new PageInfo(0, 0, 10, 0)));

    // When
    mockMvc
        .perform(
            get("/api/v1/manager/users")
                .param("email", email)
                .param("displayName", displayName)
                .param("role", role)
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id,desc"))
        .andExpect(status().isOk());

    // Then
    ArgumentCaptor<GetUsersCmd> captor = ArgumentCaptor.forClass(GetUsersCmd.class);
    verify(getUsersUseCase).operate(captor.capture());

    GetUsersCmd capturedCmd = captor.getValue();
    assertThat(capturedCmd.email()).isEqualTo(email);
    assertThat(capturedCmd.displayName()).isEqualTo(displayName);
    assertThat(capturedCmd.role().name()).isEqualTo(role);
    assertThat(capturedCmd.page()).isEqualTo(0);
    assertThat(capturedCmd.size()).isEqualTo(10);
    assertThat(capturedCmd.sort()).isEqualTo("id: DESC");
  }
}
