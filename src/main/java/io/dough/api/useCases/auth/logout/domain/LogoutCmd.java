package io.dough.api.useCases.auth.logout.domain;

import java.util.UUID;

/** 로그아웃 처리에 필요한 정보를 담는 커맨드입니다. */
public record LogoutCmd(UUID userId) {}
