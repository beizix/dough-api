package io.dough.api.useCases.user.signup.application;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.signup.application.model.SignupUser;

/** 회원가입 유스케이스에서 요구하는 영속성 작업을 정의한 출력 포트입니다. */
public interface RegisterUser {
  /**
   * 해당 이메일과 권한을 가진 사용자가 이미 존재하는지 확인합니다.
   *
   * @param email 이메일
   * @param role 권한
   * @return 존재 여부 (true: 존재함, false: 존재하지 않음)
   */
  boolean existsByEmailAndRole(String email, Role role);

  SignupUser save(String email, String password, String displayName, Role role);
}
