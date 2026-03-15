package io.dough.api.useCases.user.mgmt.updateManager.adapters.web;

import io.dough.api.useCases.user.mgmt.updateManager.adapters.web.model.UpdateManagerRequest;
import io.dough.api.useCases.user.mgmt.updateManager.adapters.web.model.UpdateManagerResponse;
import io.dough.api.useCases.user.mgmt.updateManager.application.UpdateManagerUseCase;
import io.dough.api.useCases.user.mgmt.updateManager.application.model.UpdateManagerCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 관리", description = "관리자용 사용자 목록 및 생성 관리 API")
@RestController
@RequestMapping("/api/v1/manager/users/manager")
@RequiredArgsConstructor
public class UpdateManagerWebAdapter {

  private final UpdateManagerUseCase updateManagerUseCase;

  @Operation(summary = "매니저 정보 수정", description = "이메일을 기반으로 매니저 계정의 이름 또는 비밀번호를 수정합니다.")
  @ApiResponse(responseCode = "200", description = "수정 성공")
  @PatchMapping
  public UpdateManagerResponse updateManager(@RequestBody UpdateManagerRequest request) {
    return UpdateManagerResponse.from(
        updateManagerUseCase.operate(
            new UpdateManagerCmd(request.email(), request.displayName(), request.password())));
  }
}
