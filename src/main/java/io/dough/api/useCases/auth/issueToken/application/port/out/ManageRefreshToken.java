package io.dough.api.useCases.auth.issueToken.application.port.out;

import java.util.Optional;
import java.util.UUID;

/** 리프레시 토큰의 저장 및 조회를 담당하는 출력 포트입니다. */
public interface ManageRefreshToken {

  /**
   * 리프레시 토큰과 연결된 사용자 정보를 조회합니다.
   *
   * @param refreshToken 리프레시 토큰
   * @return 유효한 토큰일 경우 사용자 정보, 그렇지 않으면 Optional.empty()
   */
  Optional<RefreshUserLoaded> loadRefreshUser(String refreshToken);

  /**
   * 리프레시 토큰을 저장하거나 업데이트합니다.
   *
   * @param uuid         사용자 식별자
   * @param refreshToken 발급된 리프레시 토큰
   */
  void updateRefreshToken(UUID uuid, String refreshToken);
}
