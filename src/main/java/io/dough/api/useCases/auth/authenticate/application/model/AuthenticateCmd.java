package io.dough.api.useCases.auth.authenticate.application.model;

import io.dough.api.useCases.shared.domain.auth.EmailValidator;
import io.dough.api.useCases.shared.domain.auth.PasswordValidator;
import io.dough.api.useCases.shared.domain.auth.Role;

/**
 * 로그인 요청 정보를 담은 커맨드 객체입니다.
 *
 * @param email 로그인할 이메일 주소
 * @param password 비밀번호
 */
public record AuthenticateCmd(String email, String password, Role role) {
  public AuthenticateCmd {
    EmailValidator.validate(email);
    PasswordValidator.validate(password);
  }
}
