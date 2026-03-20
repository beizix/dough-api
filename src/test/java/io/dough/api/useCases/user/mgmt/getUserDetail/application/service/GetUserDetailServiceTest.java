package io.dough.api.useCases.user.mgmt.getUserDetail.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.GetUserDetailCmd;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.UserDetailLoaded;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.out.LoadUserDetail;
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

  @Mock private LoadUserDetail loadUserDetail;

  @InjectMocks private GetUserDetailService getUserDetailService;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 상세 조회 시 영속성 포트가 호출되고 결과가 반환된다")
  void get_user_detail_success() {
    // Given
    UUID userId = UUID.randomUUID();
    GetUserDetailCmd cmd = new GetUserDetailCmd(userId);
    UserDetailLoaded expected =
        new UserDetailLoaded(userId, "test@example.com", "테스터", Role.USER, LocalDateTime.now());
    given(loadUserDetail.operate(cmd)).willReturn(expected);

    // When
    UserDetailLoaded actual = getUserDetailService.operate(cmd);

    // Then
    assertThat(actual).isEqualTo(expected);
    verify(loadUserDetail).operate(cmd);
  }
}
