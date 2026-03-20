package io.dough.api.useCases.user.profile.updateProfileImage.adapters.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.user.profile.updateProfileImage.application.port.in.ProfileImageUpdated;
import io.dough.api.useCases.user.profile.updateProfileImage.application.port.in.UpdateProfileImageCmd;
import io.dough.api.useCases.user.profile.updateProfileImage.application.port.in.UpdateProfileImageUseCase;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(UpdateProfileImageWebAdapter.class)
class UpdateProfileImageWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private UpdateProfileImageUseCase updateProfileImageUseCase;

  @Test
  @DisplayName("Scenario: 성공 - 멀티파트 파일을 전달하면 프로필 이미지를 저장하고 참조 URL을 포함한 정보를 반환한다")
  void update_my_profile_img_success() throws Exception {
    // Given
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "profile.png", MediaType.IMAGE_PNG_VALUE, "test image content".getBytes());

    UUID savedFileId = UUID.randomUUID();
    ProfileImageUpdated mockResult =
        new ProfileImageUpdated(
            savedFileId,
            "unique_profile.png",
            "profile.png",
            (long) file.getSize(),
            "http://example.com/profile.png");

    given(updateProfileImageUseCase.operate(any(UpdateProfileImageCmd.class)))
        .willReturn(Optional.of(mockResult));

    // When & Then
    mockMvc
        .perform(
            multipart("/api/v1/user/profile/img")
                .file(file)
                .principal(() -> UUID.randomUUID().toString()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(savedFileId.toString()))
        .andExpect(jsonPath("$.referURL").value("http://example.com/profile.png"));

    verify(updateProfileImageUseCase).operate(any(UpdateProfileImageCmd.class));
  }
}
