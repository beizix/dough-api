package io.dough.api.useCases.user.profile.updateProfileImage.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.file.resolveURL.application.ResolveURLUseCase;
import io.dough.api.useCases.file.upload.application.UploadFileUseCase;
import io.dough.api.useCases.file.upload.application.model.UploadFileCmd;
import io.dough.api.useCases.file.upload.application.model.UploadedFile;
import io.dough.api.useCases.shared.domain.file.FileUploadType;
import io.dough.api.useCases.user.profile.updateProfileImage.application.model.ProfileImageUpdated;
import io.dough.api.useCases.user.profile.updateProfileImage.application.model.UpdateProfileImageCmd;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateProfileImageServiceTest {

  @Mock private UploadFileUseCase uploadFileUseCase;
  @Mock private UpdateUserProfileImage updateUserProfileImage;
  @Mock private ResolveURLUseCase resolveURLUseCase;

  @InjectMocks private UpdateProfileImageService updateProfileImageService;

  @Test
  @DisplayName("Scenario: 성공 - 파일을 저장하고 사용자 정보 업데이트 및 참조 URL을 조회하여 반환한다")
  void update_profile_image_service_success() {
    // Given
    UUID userId = UUID.randomUUID();
    byte[] content = "test content".getBytes();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
    UpdateProfileImageCmd cmd =
        new UpdateProfileImageCmd(userId, inputStream, "profile.png", (long) content.length);

    UUID savedFileId = UUID.randomUUID();
    UploadedFile mockUploadedFile =
        new UploadedFile(
            savedFileId,
            FileUploadType.MY_PROFILE_IMG,
            "/user/profile/img",
            "unique_profile.png",
            "profile.png",
            (long) content.length);

    String expectedUrl = "http://example.com/files/" + savedFileId;

    given(uploadFileUseCase.operate(any(UploadFileCmd.class))).willReturn(mockUploadedFile);
    given(resolveURLUseCase.operate(savedFileId)).willReturn(expectedUrl);

    // When
    Optional<ProfileImageUpdated> result = updateProfileImageService.operate(cmd);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get().referURL()).isEqualTo(expectedUrl);
    verify(uploadFileUseCase).operate(any(UploadFileCmd.class));
    verify(updateUserProfileImage).operate(userId, savedFileId);
    verify(resolveURLUseCase).operate(savedFileId);
  }
}
