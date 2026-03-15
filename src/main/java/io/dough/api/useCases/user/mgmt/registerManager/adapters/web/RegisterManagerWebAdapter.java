package io.dough.api.useCases.user.mgmt.registerManager.adapters.web;

import io.dough.api.useCases.user.mgmt.registerManager.adapters.web.model.RegisterManagerRequest;
import io.dough.api.useCases.user.mgmt.registerManager.adapters.web.model.RegisterManagerResponse;
import io.dough.api.useCases.user.mgmt.registerManager.application.RegisterManagerUseCase;
import io.dough.api.useCases.user.mgmt.registerManager.application.model.RegisterManagerCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 관리", description = "관리자용 사용자 목록 및 생성 관리 API")
@RestController
@RequestMapping("/api/v1/manager/users/manager")
@RequiredArgsConstructor
public class RegisterManagerWebAdapter {

  private final RegisterManagerUseCase registerManagerUseCase;

  @Operation(summary = "매니저 생성", description = "이메일과 이름을 기반으로 매니저 계정을 생성합니다.")
  @ApiResponse(responseCode = "200", description = "생성 성공")
  @PostMapping
  public RegisterManagerResponse createManager(@RequestBody RegisterManagerRequest request) {
    return RegisterManagerResponse.from(
        registerManagerUseCase.operate(
            new RegisterManagerCmd(request.email(), request.displayName(), request.password())));
  }
}
