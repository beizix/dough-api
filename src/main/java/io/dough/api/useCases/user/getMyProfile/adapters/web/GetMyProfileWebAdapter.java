package io.dough.api.useCases.user.getMyProfile.adapters.web;

import io.dough.api.useCases.user.getMyProfile.adapters.web.model.GetMyProfileResponse;
import io.dough.api.useCases.user.getMyProfile.application.GetMyProfileUseCase;
import io.dough.api.useCases.user.getMyProfile.application.domain.model.GetMyProfileCmd;
import io.dough.api.useCases.user.getMyProfile.application.domain.model.MyProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 정보", description = "로그인한 사용자 관련 API")
@RestController
@RequestMapping("/api/v1/user/profile")
@RequiredArgsConstructor
public class GetMyProfileWebAdapter {

  private final GetMyProfileUseCase getMyProfileUseCase;

  @Operation(summary = "내 정보 조회", description = "로그인된 사용자의 상세 정보를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공")
  @GetMapping
  public ResponseEntity<GetMyProfileResponse> getMyProfile(Principal principal) {
    // Principal.getName() returns the UUID string as set in JwtAuthenticationFilter
    UUID userId = UUID.fromString(principal.getName());
    GetMyProfileCmd cmd = new GetMyProfileCmd(userId);
    MyProfile result = getMyProfileUseCase.operate(cmd);

    GetMyProfileResponse response =
        new GetMyProfileResponse(
            result.id(),
            result.email(),
            result.displayName(),
            result.role(),
            result.createdAt(),
            result.profileImageUrl());

    return ResponseEntity.ok(response);
  }
}
