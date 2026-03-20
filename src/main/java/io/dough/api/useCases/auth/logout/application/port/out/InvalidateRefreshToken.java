package io.dough.api.useCases.auth.logout.application.port.out;

import java.util.UUID;

/** 리프레시 토큰 무효화를 담당하는 출력 포트입니다. */
public interface InvalidateRefreshToken {
  /**
   * 해당 식별자를 가진 사용자의 리프레시 토큰을 제거합니다.
   *
   * @param userId 사용자 식별자 (UUID)
   */
  void operate(UUID userId);
}
