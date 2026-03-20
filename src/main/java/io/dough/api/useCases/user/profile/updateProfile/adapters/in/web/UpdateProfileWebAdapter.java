package io.dough.api.useCases.user.profile.updateProfile.adapters.in.web;

import io.dough.api.useCases.user.profile.updateProfile.application.port.in.ProfileUpdated;
import io.dough.api.useCases.user.profile.updateProfile.application.port.in.UpdateProfileCmd;
import io.dough.api.useCases.user.profile.updateProfile.application.port.in.UpdateProfileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Profile", description = "사용자 프로필 관련 API")
@RestController
@RequiredArgsConstructor
class UpdateProfileWebAdapter {
  private final UpdateProfileUseCase updateProfileUseCase;

  @Operation(
      summary = "사용자 프로필 업데이트",
      description = "현재 로그인된 사용자의 프로필 정보를 업데이트합니다.",
      tags = {"Profile"})
  @ApiResponse(responseCode = "200", description = "프로필 업데이트 성공")
  @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
  @ApiResponse(responseCode = "401", description = "인증 실패")
  @PatchMapping({"/api/v1/user/profile", "/api/v1/manager/profile"})
  @ResponseStatus(HttpStatus.OK)
  public ProfileUpdated updateUserProfile(
      @Parameter(hidden = true) Principal principal,
      @RequestBody @Parameter(description = "업데이트할 프로필 정보", required = true)
          UpdateProfileRequest request) {
    UUID loginUserId = UUID.fromString(principal.getName());
    UpdateProfileCmd cmd =
        new UpdateProfileCmd(loginUserId, request.email(), request.displayName());
    return updateProfileUseCase.operate(cmd);
  }
}
