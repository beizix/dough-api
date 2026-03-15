package io.dough.api.useCases.user.profile.retrieveProfile.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.profile.retrieveProfile.application.RetrieveProfileUseCase;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.RetrieveProfile;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.RetrieveProfileCmd;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(RetrieveProfileWebAdapter.class)
class RetrieveProfileWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private RetrieveProfileUseCase retrieveProfileUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 로그인된 사용자의 상세 정보를 반환한다")
  void retrieve_my_profile_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    String email = "test@example.com";
    RetrieveProfile expectedUser =
        new RetrieveProfile(
            userId,
            email,
            "Test User",
            LocalDateTime.now(),
            null, // profileImageId
            "http://example.com/profile.png");

    given(retrieveProfileUseCase.operate(any(RetrieveProfileCmd.class))).willReturn(expectedUser);

    // When & Then
    mockMvc
        .perform(
            get("/api/v1/user/profile")
                .principal(() -> userId.toString())) // Mocking ID as Principal
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userId.toString()))
        .andExpect(jsonPath("$.email").value(email))
        .andExpect(jsonPath("$.displayName").value("Test User"))
        .andExpect(jsonPath("$.profileImageUrl").value("http://example.com/profile.png"))
        .andExpect(jsonPath("$.createdAt").exists());

    verify(retrieveProfileUseCase).operate(any(RetrieveProfileCmd.class));
  }
}
