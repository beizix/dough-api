package io.dough.api.useCases.user.registerUser.adapters.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisterUserCmd;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisterUserUseCase;
import io.dough.api.useCases.user.registerUser.application.port.in.RegisteredToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(RegisterUserWebAdapter.class)
class RegisterUserWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private RegisterUserUseCase registerUserUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 회원가입 요청 시 토큰을 반환한다")
  void signup_user_success() throws Exception {
    // Given
    RegisterUserRequest req =
        new RegisterUserRequest("user@dough.io", "password123!", "User Nickname");
    given(registerUserUseCase.operate(any(RegisterUserCmd.class)))
        .willReturn(new RegisteredToken("access_token_value", "refresh_token_value"));

    // When
    mockMvc
        .perform(
            post("/api/v1/signup/user")
                .content(json(req))
                .contentType("application/json"))
        .andExpect(status().isOk());

    // Then
    verify(registerUserUseCase).operate(any(RegisterUserCmd.class));
  }
}
