package io.dough.api.useCases.user.profile.retrieveProfile.adapters.web;

import io.dough.api.useCases.user.profile.retrieveProfile.adapters.web.model.RetrieveProfileResponse;
import io.dough.api.useCases.user.profile.retrieveProfile.application.RetrieveProfileUseCase;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.RetrieveProfile;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.RetrieveProfileCmd;
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

@Tag(name = "Profile", description = "사용자 프로필 관련 API")
@RestController
@RequestMapping({"/api/v1/user/profile", "/api/v1/manager/profile"})
@RequiredArgsConstructor
public class RetrieveProfileWebAdapter {

  private final RetrieveProfileUseCase retrieveProfileUseCase;

  @Operation(summary = "내 정보 조회", description = "로그인된 사용자의 상세 정보를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "성공")
  @GetMapping
  public ResponseEntity<RetrieveProfileResponse> retrieveMyProfile(Principal principal) {
    UUID userId = UUID.fromString(principal.getName());
    RetrieveProfileCmd cmd = new RetrieveProfileCmd(userId);
    RetrieveProfile result = retrieveProfileUseCase.operate(cmd);

    RetrieveProfileResponse response =
        new RetrieveProfileResponse(
            result.id(),
            result.email(),
            result.displayName(),
            result.createdAt(),
            result.profileImageUrl());

    return ResponseEntity.ok(response);
  }
}
