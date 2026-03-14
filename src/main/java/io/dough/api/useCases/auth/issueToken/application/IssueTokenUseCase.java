package io.dough.api.useCases.auth.issueToken.application;

import io.dough.api.useCases.auth.issueToken.application.model.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.model.IssueTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.model.RefreshTokenCmd;

/** 인증 토큰(JWT)의 발급 및 갱신을 담당하는 유스케이스입니다. */
public interface IssueTokenUseCase {
  /**
   * 사용자 정보를 기반으로 새로운 인증 토큰 세트를 생성합니다.
   *
   * @param cmd 토큰 생성에 필요한 사용자 정보 (이메일, 이름, 권한)
   * @return 생성된 액세스 토큰과 리프레시 토큰
   */
  AuthToken createToken(IssueTokenCmd cmd);

  /**
   * 리프레시 토큰을 사용하여 새로운 토큰 세트를 발급합니다.
   *
   * @param cmd 리프레시 토큰 정보
   * @return 갱신된 액세스 토큰과 리프레시 토큰
   */
  AuthToken refreshToken(RefreshTokenCmd cmd);
}
