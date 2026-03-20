package io.dough.api.useCases.user.mgmt.getUserDetail.adapters.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.GetUserDetailUseCase;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.UserDetailLoaded;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(GetUserDetailWebAdapter.class)
class GetUserDetailWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private GetUserDetailUseCase getUserDetailUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 ID로 상세 조회 시 UseCase가 호출된다")
  void get_user_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    UserDetailLoaded userDetailLoaded =
        new UserDetailLoaded(userId, "test@example.com", "테스터", Role.USER, LocalDateTime.now());
    given(getUserDetailUseCase.operate(any())).willReturn(userDetailLoaded);

    // When
    mockMvc.perform(get("/api/v1/manager/users/{id}", userId)).andExpect(status().isOk());

    // Then
    verify(getUserDetailUseCase).operate(any());
  }
}
