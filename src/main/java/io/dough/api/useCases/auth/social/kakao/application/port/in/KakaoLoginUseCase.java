package io.dough.api.useCases.auth.social.kakao.application.port.in;

import io.dough.api.useCases.auth.authenticate.application.port.in.AuthenticatedToken;

public interface KakaoLoginUseCase {
  AuthenticatedToken operate(KakaoLoginCmd cmd);
}
