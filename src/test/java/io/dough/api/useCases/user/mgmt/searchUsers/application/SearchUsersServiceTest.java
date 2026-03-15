package io.dough.api.useCases.user.mgmt.searchUsers.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.searchUsers.application.model.SearchUsers;
import io.dough.api.useCases.user.mgmt.searchUsers.application.model.SearchUsersCmd;
import io.dough.api.useCases.shared.application.model.PageInfo;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SearchUsersServiceTest {

  @Mock private FindUsers findUsers;

  @InjectMocks private SearchUsersService searchUsersService;

  @Test
  @DisplayName("Scenario: 성공 - 서비스 호출 시 영속성 포트가 호출되고 결과가 반환된다")
  void operate_success() {
    // Given
    SearchUsersCmd cmd = new SearchUsersCmd("test@example.com", "테스터", Role.USER, 0, 10, "id: DESC");
    SearchUsers expectedResponse = new SearchUsers(List.of(), new PageInfo(0, 0, 10, 0));
    given(findUsers.operate(cmd)).willReturn(expectedResponse);

    // When
    SearchUsers actualResponse = searchUsersService.operate(cmd);

    // Then
    assertThat(actualResponse).isEqualTo(expectedResponse);
    verify(findUsers).operate(cmd);
  }
}
