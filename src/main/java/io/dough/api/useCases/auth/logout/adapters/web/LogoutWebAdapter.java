package io.dough.api.useCases.auth.logout.adapters.web;

import io.dough.api.useCases.auth.logout.application.LogoutUseCase;
import io.dough.api.useCases.auth.logout.application.model.LogoutCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LogoutWebAdapter {

  private final LogoutUseCase logoutUseCase;

  @Operation(summary = "사용자/매니저 로그아웃", description = "현재 사용자의 리프레시 토큰을 무효화하여 로그아웃 처리합니다.")
  @ApiResponse(responseCode = "200", description = "성공")
  @PostMapping("/logout")
  public void logout(Principal principal) {
    logoutUseCase.operate(new LogoutCmd(UUID.fromString(principal.getName())));
  }
}
