package io.dough.api.useCases.user.profile.updateProfile.adapters.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.profile.updateProfile.application.port.in.UpdateProfileCmd;
import io.dough.api.useCases.user.profile.updateProfile.application.port.in.UpdateProfileUseCase;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(UpdateProfileWebAdapter.class)
class UpdateProfileWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private UpdateProfileUseCase updateProfileUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 프로필을 성공적으로 업데이트한다")
  void update_user_profile_success() throws Exception {
    // Given
    UUID userId = UUID.randomUUID();
    UpdateProfileRequest request = new UpdateProfileRequest("new.email@example.com", "New Name");

    // When & Then
    mockMvc
        .perform(
            patch("/api/v1/user/profile")
                .principal(() -> userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
        .andDo(print())
        .andExpect(status().isOk());

    verify(updateProfileUseCase).operate(any(UpdateProfileCmd.class));
  }
}
