package io.dough.api.useCases.auth.issueToken.application.port.out;

import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.UUID;

/**
 * 리프레시 토큰을 통해 갱신 대상 사용자 정보를 조회하기 위한 모델입니다.
 *
 * @param uuid 사용자 식별자
 * @param email 사용자 이메일
 * @param displayName 사용자 표시 이름
 * @param role 사용자 권한
 */
public record RefreshUserLoaded(UUID uuid, String email, String displayName, Role role) {}
