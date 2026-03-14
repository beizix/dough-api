package io.dough.api.useCases.auth.issueToken.application.model;

import io.dough.api.useCases.shared.domain.auth.DisplayNameValidator;
import io.dough.api.useCases.shared.domain.auth.EmailValidator;
import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.UUID;

/**
 * 신규 토큰 생성에 필요한 정보를 담은 커맨드 객체입니다.
 *
 * @param uuid 사용자 식별자 (Subject)
 * @param email 사용자 이메일 (Claim)
 * @param displayName 사용자 표시 이름
 * @param role 사용자 권한
 */
public record CreateTokenCmd(UUID uuid, String email, String displayName, Role role) {
  public CreateTokenCmd {
    EmailValidator.validate(email);
    DisplayNameValidator.validate(displayName);
  }
}
