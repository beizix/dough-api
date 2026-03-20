package io.dough.api.useCases.user.profile.updateProfileImage.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.FileMetadataEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.FileMetadataRepository;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.shared.domain.file.FileUploadType;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(UpdateProfileImagePersistAdapter.class)
class UpdateProfileImagePersistAdapterTest extends DataJpaTestBase {

  @Autowired
  private UpdateProfileImagePersistAdapter adapter;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FileMetadataRepository fileMetadataRepository;

  @Test
  @DisplayName("Scenario: 성공 - 사용자의 프로필 이미지를 업데이트한다")
  void update_profile_image_success() {
    // Given
    UserEntity user = new UserEntity(
        "test@example.com", "password", "Tester", Role.USER, null);
    userRepository.save(user);

    FileMetadataEntity fileMetadata = FileMetadataEntity.builder()
        .id(UUID.randomUUID())
        .type(FileUploadType.MY_PROFILE_IMG)
        .path("/test/path")
        .name("test.png")
        .originName("test.png")
        .fileLength(1024L)
        .build();
    fileMetadataRepository.save(fileMetadata);

    // When
    adapter.operate(user.getId(), fileMetadata.getId());

    // Then
    UserEntity updatedUser = userRepository.findById(user.getId()).orElseThrow();
    assertThat(updatedUser.getProfileImage()).isNotNull();
    assertThat(updatedUser.getProfileImage().getId()).isEqualTo(fileMetadata.getId());
  }

  @Test
  @DisplayName("Scenario: 실패 - 존재하지 않는 사용자의 프로필 이미지를 업데이트 시 예외가 발생한다")
  void update_profile_image_fail_user_not_found() {
    // Given
    UUID nonExistentUserId = UUID.randomUUID();
    UUID fileId = UUID.randomUUID();

    // When & Then
    assertThatThrownBy(() -> adapter.operate(nonExistentUserId, fileId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("User not found");
  }

  @Test
  @DisplayName("Scenario: 실패 - 존재하지 않는 파일 메타데이터로 업데이트 시 예외가 발생한다")
  void update_profile_image_fail_file_not_found() {
    // Given
    UserEntity user = new UserEntity(
        "test@example.com", "password", "Tester", Role.USER, null);
    userRepository.save(user);
    UUID nonExistentFileId = UUID.randomUUID();

    // When & Then
    assertThatThrownBy(() -> adapter.operate(user.getId(), nonExistentFileId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("File metadata not found");
  }
}
