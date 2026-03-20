package io.dough.api.useCases.user.mgmt.getUserDetail.adapters.in.web;

import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.GetUserDetailCmd;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.GetUserDetailUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 관리", description = "관리자용 사용자 목록 및 상세 관리 API")
@RestController
@RequestMapping("/api/v1/manager/users")
@RequiredArgsConstructor
public class GetUserDetailWebAdapter {

  private final GetUserDetailUseCase getUserDetailUseCase;

  @Operation(summary = "사용자 상세 조회", description = "ID(UUID)를 기반으로 특정 사용자의 상세 정보를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  @ApiResponse(responseCode = "400", description = "잘못된 요청 (ID 형식 오류 등)")
  @GetMapping("/{id}")
  public GetUserDetailResponse getUser(
      @Parameter(description = "사용자 UUID", required = true) @PathVariable UUID id) {
    return GetUserDetailResponse.from(getUserDetailUseCase.operate(new GetUserDetailCmd(id)));
  }
}
