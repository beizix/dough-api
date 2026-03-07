package io.dough.api.useCases.user.maintenance.getUser.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.getUser.domain.GetUserCmd;
import io.dough.api.useCases.user.maintenance.getUser.domain.UserDetail;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserDetailServiceTest {

  @Mock private LoadUser loadUser;

  @InjectMocks private GetUserDetailService getUserDetailService;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 상세 조회 시 영속성 포트가 호출되고 결과가 반환된다")
  void get_user_detail_success() {
    // Given
    UUID userId = UUID.randomUUID();
    GetUserCmd cmd = new GetUserCmd(userId);
    UserDetail expected =
        new UserDetail(userId, "test@example.com", "테스터", Role.USER, LocalDateTime.now());
    given(loadUser.operate(cmd)).willReturn(expected);

    // When
    UserDetail actual = getUserDetailService.operate(cmd);

    // Then
    assertThat(actual).isEqualTo(expected);
    verify(loadUser).operate(cmd);
  }
}
