package io.dough.api.useCases.auth.logout.application.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.auth.logout.application.port.in.LogoutCmd;
import io.dough.api.useCases.auth.logout.application.port.out.InvalidateRefreshToken;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LogoutServiceTest {

  private LogoutService logoutService;
  private InvalidateRefreshToken invalidateRefreshToken;

  @BeforeEach
  void setUp() {
    invalidateRefreshToken = mock(InvalidateRefreshToken.class);
    logoutService = new LogoutService(invalidateRefreshToken);
  }

  @Test
  @DisplayName("Scenario: 성공 - 로그아웃 서비스 실행 시 리프레시 토큰 무효화 포트를 호출한다")
  void logout_success() {
    // Given
    UUID userId = UUID.randomUUID();
    LogoutCmd cmd = new LogoutCmd(userId);

    // When
    logoutService.operate(cmd);

    // Then
    verify(invalidateRefreshToken).operate(cmd.userId());
  }
}
