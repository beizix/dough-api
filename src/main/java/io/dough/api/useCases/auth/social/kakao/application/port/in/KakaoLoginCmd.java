package io.dough.api.useCases.auth.social.kakao.application.port.in;

import io.dough.api.useCases.shared.domain.auth.Role;

public record KakaoLoginCmd(String code, Role role) {}
