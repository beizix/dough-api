package io.dough.api.useCases.auth.resolveToken.adapters.web;

import io.dough.api.useCases.auth.resolveToken.adapters.web.model.ValidateRequest;
import io.dough.api.useCases.auth.resolveToken.adapters.web.model.ValidateResponse;
import io.dough.api.useCases.auth.resolveToken.application.ResolveTokenUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰 관리", description = "토큰 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ValidateTokenWebAdapter {

  private final ResolveTokenUseCase resolveTokenUseCase;

  @Operation(summary = "토큰 검증", description = "액세스 토큰의 유효성을 검증합니다.")
  @ApiResponse(responseCode = "200", description = "토큰 검증 결과 반환")
  @PostMapping("/validate")
  public ValidateResponse validate(
      @RequestBody @Parameter(description = "토큰 검증 요청", required = true) ValidateRequest req) {
    boolean isValid = resolveTokenUseCase.validateToken(req.token());
    return new ValidateResponse(isValid);
  }
}
