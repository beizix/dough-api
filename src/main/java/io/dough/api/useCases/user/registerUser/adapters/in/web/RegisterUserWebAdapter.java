package io.dough.api.useCases.user.registerUser.adapters.in.web;

import io.dough.api.useCases.user.registerUser.application.port.in.RegisterUserCmd;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisterUserUseCase;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisteredToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원가입", description = "회원가입 관련 API")
@RestController
@RequiredArgsConstructor
class RegisterUserWebAdapter {

  private final RegisterUserUseCase registerUserUseCase;

  @Operation(summary = "사용자 회원가입", description = "일반 사용자 계정을 생성하고 토큰을 발급합니다.")
  @ApiResponse(responseCode = "200", description = "회원가입 성공")
  @PostMapping("/api/v1/signup/user")
  public RegisterUserResponse signupUser(
      @RequestBody @Parameter(description = "사용자 가입 정보", required = true) RegisterUserRequest req) {
    RegisteredToken token =
        registerUserUseCase.operate(
            new RegisterUserCmd(req.email(), req.password(), req.displayName()));
    return new RegisterUserResponse(token.accessToken(), token.refreshToken());
  }
}
