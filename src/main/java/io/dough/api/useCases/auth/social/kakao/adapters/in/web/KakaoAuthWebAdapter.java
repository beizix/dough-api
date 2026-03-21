package io.dough.api.useCases.auth.social.kakao.adapters.in.web;

import io.dough.api.useCases.auth.authenticate.adapters.in.web.AuthenticateResponse;
import io.dough.api.useCases.auth.authenticate.application.port.in.AuthenticatedToken;
import io.dough.api.useCases.auth.social.kakao.application.port.in.KakaoLoginCmd;
import io.dough.api.useCases.auth.social.kakao.application.port.in.KakaoLoginUseCase;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
public class KakaoAuthWebAdapter {

  private final KakaoLoginUseCase kakaoLoginUseCase;

  @Operation(summary = "카카오 로그인")
  @GetMapping("/api/v1/auth/login/kakao")
  public AuthenticateResponse operate(@RequestParam("code") String code) {
    AuthenticatedToken token =
        kakaoLoginUseCase.operate(new KakaoLoginCmd(code, Role.USER));
    return new AuthenticateResponse(token.accessToken(), token.refreshToken());
  }
}
