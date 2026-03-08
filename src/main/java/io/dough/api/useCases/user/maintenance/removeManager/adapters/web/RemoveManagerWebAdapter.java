package io.dough.api.useCases.user.maintenance.removeManager.adapters.web;

import io.dough.api.useCases.user.maintenance.removeManager.adapters.web.model.RemoveManagerRequest;
import io.dough.api.useCases.user.maintenance.removeManager.adapters.web.model.RemoveManagerResponse;
import io.dough.api.useCases.user.maintenance.removeManager.application.RemoveManagerUseCase;
import io.dough.api.useCases.user.maintenance.removeManager.domain.RemoveManagerCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 관리", description = "관리자용 사용자 목록 및 관리 API")
@RestController
@RequestMapping("/api/v1/manager/users/manager")
@RequiredArgsConstructor
public class RemoveManagerWebAdapter {

  private final RemoveManagerUseCase removeManagerUseCase;

  @Operation(summary = "매니저 삭제", description = "ID(UUID)를 기반으로 매니저를 삭제(Soft Delete)합니다.")
  @ApiResponse(responseCode = "200", description = "삭제 성공")
  @DeleteMapping
  public RemoveManagerResponse removeManager(
      @RequestBody RemoveManagerRequest request, java.security.Principal principal) {
    return RemoveManagerResponse.from(
        removeManagerUseCase.operate(
            new RemoveManagerCmd(java.util.UUID.fromString(request.id()), principal.getName())));
  }
}
