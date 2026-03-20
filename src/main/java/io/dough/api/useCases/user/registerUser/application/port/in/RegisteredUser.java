package io.dough.api.useCases.user.registerUser.application.port.in;

import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.UUID;

/**
 * 회원가입 프로세스 내에서 사용되는 도메인 모델입니다.
 *
 * @param id 사용자 고유 식별자
 * @param email 사용자 이메일
 * @param password 암호화된 비밀번호
 * @param displayName 사용자 표시 이름
 * @param role 사용자 권한
 */
public record RegisteredUser(
    UUID id, String email, String password, String displayName, Role role) {}
