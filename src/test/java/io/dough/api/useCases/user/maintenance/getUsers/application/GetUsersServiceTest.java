package io.dough.api.useCases.user.maintenance.getUsers.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.getUsers.application.model.GetUsers;
import io.dough.api.useCases.user.maintenance.getUsers.application.model.GetUsersCmd;
import io.dough.api.useCases.user.maintenance.getUsers.application.model.PageInfo;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUsersServiceTest {

  @Mock private LoadUsers loadUsers;

  @InjectMocks private GetUsersService getUsersService;

  @Test
  @DisplayName("Scenario: 성공 - 서비스 호출 시 영속성 포트가 호출되고 결과가 반환된다")
  void operate_success() {
    // Given
    GetUsersCmd cmd = new GetUsersCmd("test@example.com", "테스터", Role.USER, 0, 10, "id: DESC");
    GetUsers expectedResponse = new GetUsers(List.of(), new PageInfo(0, 0, 10, 0));
    given(loadUsers.operate(cmd)).willReturn(expectedResponse);

    // When
    GetUsers actualResponse = getUsersService.operate(cmd);

    // Then
    assertThat(actualResponse).isEqualTo(expectedResponse);
    verify(loadUsers).operate(cmd);
  }
}
