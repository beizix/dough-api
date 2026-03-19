package io.dough.api.useCases.auth.issueToken.application.port.out;

import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.UUID;

/** 토큰(JWT) 생성을 담당하는 인터페이스입니다. */
public interface TokenProvider {
  /**
   * 액세스 토큰을 생성합니다.
   *
   * @param uuid 사용자 식별자
   * @param email 사용자 이메일
   * @param displayName 사용자 이름
   * @param role 사용자 권한
   * @return 생성된 액세스 토큰
   */
  String getAccessToken(UUID uuid, String email, String displayName, Role role);

  /**
   * 리프레시 토큰을 생성합니다.
   *
   * @param uuid 사용자 식별자
   * @param email 사용자 이메일
   * @param displayName 사용자 이름
   * @param role 사용자 권한
   * @return 생성된 리프레시 토큰
   */
  String getRefreshToken(UUID uuid, String email, String displayName, Role role);
}
