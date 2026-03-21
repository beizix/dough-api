package io.dough.api.useCases.auth.social.kakao.application.service;

import io.dough.api.useCases.auth.authenticate.application.port.in.AuthenticatedToken;
import io.dough.api.useCases.auth.authenticate.application.port.out.AuthenticatableUser;
import io.dough.api.useCases.auth.authenticate.application.port.out.LoadAuthenticatableUser;
import io.dough.api.useCases.auth.issueToken.application.port.in.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenCmd;
import io.dough.api.useCases.auth.issueToken.application.port.in.IssueTokenUseCase;
import io.dough.api.useCases.auth.social.kakao.adapters.out.sns.KakaoAuthClient;
import io.dough.api.useCases.auth.social.kakao.adapters.out.sns.KakaoTokenResponse;
import io.dough.api.useCases.auth.social.kakao.adapters.out.sns.KakaoUserInfo;
import io.dough.api.useCases.auth.social.kakao.application.port.in.KakaoLoginCmd;
import io.dough.api.useCases.auth.social.kakao.application.port.in.KakaoLoginUseCase;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisterUserCmd;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisterUserUseCase;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisteredToken;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService implements KakaoLoginUseCase {

  private final KakaoAuthClient kakaoAuthClient;
  private final LoadAuthenticatableUser loadAuthenticatableUser;
  private final IssueTokenUseCase issueTokenUseCase;
  private final RegisterUserUseCase registerUserUseCase;

  @Override
  @Transactional
  public AuthenticatedToken operate(KakaoLoginCmd cmd) {
    // 1. 카카오 토큰 요청
    KakaoTokenResponse tokenResponse = kakaoAuthClient.fetchToken(cmd.code());

    // 2. 카카오 사용자 정보 조회
    KakaoUserInfo userInfo = kakaoAuthClient.fetchUserInfo(tokenResponse.getAccessToken());
    String email = userInfo.getKakaoAccount().getEmail();
    String nickname = userInfo.getKakaoAccount().getProfile().getNickname();

    if (email == null || email.isBlank()) {
      // 이메일 권한이 없을 경우 카카오 ID 기반 가상 이메일 생성
      email = userInfo.getId() + "@kakao.user";
    }

    // 3. 기존 사용자 연동 확인
    Optional<AuthenticatableUser> existingUser =
        loadAuthenticatableUser.operate(email, cmd.role());

    if (existingUser.isPresent()) {
      AuthenticatableUser authUser = existingUser.get();
      AuthToken authToken =
          issueTokenUseCase.createToken(
              new IssueTokenCmd(
                  authUser.id(), authUser.email(), authUser.displayName(), authUser.role()));
      return new AuthenticatedToken(authToken.accessToken(), authToken.refreshToken());
    } else {
      // 4. 신규 가입 (패스워드는 임시 UUID)
      RegisteredToken registeredToken =
          registerUserUseCase.operate(
              new RegisterUserCmd(email, UUID.randomUUID().toString(), nickname));
      return new AuthenticatedToken(
          registeredToken.accessToken(), registeredToken.refreshToken());
    }
  }
}
