package io.dough.api.useCases.user.mgmt.searchUsers.adapters.in.web;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.searchUsers.adapters.in.web.GetUsersResponse;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsersUseCase;
import io.dough.api.useCases.user.mgmt.searchUsers.application.port.in.SearchUsersCmd;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 관리", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/v1/manager/users")
@RequiredArgsConstructor
public class GetUsersWebAdapter {

  private final SearchUsersUseCase searchUsersUseCase;

  @Operation(summary = "사용자 목록 조회", description = "필터 조건 및 페이징 정보를 기반으로 사용자 목록을 조회합니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  @GetMapping
  public GetUsersResponse getUsers(
      @Parameter(description = "이메일 검색 (LIKE)") @RequestParam(required = false) String email,
      @Parameter(description = "이름 검색 (LIKE)") @RequestParam(required = false) String displayName,
      @Parameter(description = "역할 필터") @RequestParam(required = false) Role role,
      @ParameterObject Pageable pageable) {

    SearchUsersCmd cmd =
        new SearchUsersCmd(
            email,
            displayName,
            role,
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSort().toString());

    return GetUsersResponse.from(searchUsersUseCase.operate(cmd));
  }
}
