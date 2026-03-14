package io.dough.api.useCases.user.signup.application.model;

import io.dough.api.useCases.shared.domain.auth.DisplayNameValidator;
import io.dough.api.useCases.shared.domain.auth.EmailValidator;
import io.dough.api.useCases.shared.domain.auth.PasswordValidator;
import io.dough.api.useCases.shared.domain.auth.Role;

/**
 * 회원가입에 필요한 사용자 정보를 담은 커맨드 객체입니다. 일반 사용자 전용이므로 권한은 항상 USER로 고정됩니다.
 *
 * @param email 사용자 이메일 (로그인 ID로 사용)
 * @param password 비밀번호 (암호화되어 저장됨)
 * @param displayName 서비스에서 표시될 사용자 이름
 */
public record SignupCmd(String email, String password, String displayName) {
  public SignupCmd {
    EmailValidator.validate(email);
    DisplayNameValidator.validate(displayName);
    PasswordValidator.validate(password);
  }

  public Role role() {
    return Role.USER;
  }
}
