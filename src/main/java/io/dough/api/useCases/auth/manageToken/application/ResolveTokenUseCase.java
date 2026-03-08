package io.dough.api.useCases.auth.manageToken.application;

import java.util.List;

/** 인증 토큰(JWT)의 검증 및 클레임 정보 추출을 담당하는 유스케이스입니다. */
public interface ResolveTokenUseCase {
  /**
   * 토큰의 유효성을 검증합니다.
   *
   * @param token 검증할 토큰 문자열
   * @return 유효한 토큰인 경우 true, 그렇지 않은 경우 false
   */
  boolean validateToken(String token);

  /**
   * 토큰에서 사용자의 식별자(Subject, 이메일)를 추출합니다.
   *
   * @param token 토큰 문자열
   * @return 사용자 식별자
   */
  String getSubject(String token);

  /**
   * 토큰에서 사용자의 표시 이름을 추출합니다.
   *
   * @param token 토큰 문자열
   * @return 사용자 이름
   */
  String getDisplayName(String token);

  /**
   * 토큰에서 사용자의 이메일을 추출합니다.
   *
   * @param token 토큰 문자열
   * @return 사용자 이메일
   */
  String getEmail(String token);

  /**
   * 토큰에서 사용자의 권한(Role) 정보를 추출합니다.
   *
   * @param token 토큰 문자열
   * @return 사용자 권한
   */
  String getRole(String token);

  /**
   * 토큰에서 사용자가 보유한 세부 특권(Privileges) 목록을 추출합니다.
   *
   * @param token 토큰 문자열
   * @return 특권 목록
   */
  List<String> getPrivileges(String token);
}
