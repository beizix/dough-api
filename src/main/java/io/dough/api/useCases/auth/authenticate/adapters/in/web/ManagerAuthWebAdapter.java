package io.dough.api.useCases.auth.authenticate.adapters.in.web;

import io.dough.api.useCases.auth.authenticate.application.port.in.AuthenticateCmd;
import io.dough.api.useCases.auth.authenticate.application.port.in.AuthenticateUseCase;
import io.dough.api.useCases.auth.authenticate.application.port.in.AuthenticatedToken;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
public class ManagerAuthWebAdapter {

  private final AuthenticateUseCase authenticateUseCase;

  @Operation(summary = "매니저 로그인")
  @ApiResponse(responseCode = "200", description = "로그인 성공")
  @PostMapping("/api/v1/auth/login/manager")
  public AuthenticateResponse operate(
      @RequestBody @Parameter(description = "로그인 요청", required = true) AuthenticateRequest req) {
    AuthenticatedToken token =
        authenticateUseCase.operate(new AuthenticateCmd(req.email(), req.password(), Role.MANAGER));
    return new AuthenticateResponse(token.accessToken(), token.refreshToken());
  }
}
