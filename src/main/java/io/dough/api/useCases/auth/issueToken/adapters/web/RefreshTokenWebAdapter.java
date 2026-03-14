package io.dough.api.useCases.auth.issueToken.adapters.web;

import io.dough.api.useCases.auth.issueToken.adapters.web.model.RefreshTokenRequest;
import io.dough.api.useCases.auth.issueToken.adapters.web.model.RefreshTokenResponse;
import io.dough.api.useCases.auth.issueToken.application.IssueTokenUseCase;
import io.dough.api.useCases.auth.issueToken.application.model.AuthToken;
import io.dough.api.useCases.auth.issueToken.application.model.RefreshTokenCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰 관리", description = "토큰 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RefreshTokenWebAdapter {

  private final IssueTokenUseCase issueTokenUseCase;

  @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 액세스 토큰을 갱신합니다.")
  @ApiResponse(responseCode = "200", description = "토큰 갱신 성공")
  @PostMapping("/refresh")
  public RefreshTokenResponse refresh(
      @RequestBody @Parameter(description = "토큰 갱신 요청", required = true) RefreshTokenRequest req) {
    AuthToken token = issueTokenUseCase.refreshToken(new RefreshTokenCmd(req.refreshToken()));
    return new RefreshTokenResponse(token.accessToken(), token.refreshToken());
  }
}
