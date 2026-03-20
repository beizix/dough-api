package io.dough.api.useCases.user.profile.retrieveProfile.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.file.resolveURL.application.port.in.ResolveURLUseCase;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.ProfileLoaded;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.RetrieveProfile;
import io.dough.api.useCases.user.profile.retrieveProfile.application.model.RetrieveProfileCmd;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetrieveProfileServiceTest {

  @Mock private LoadProfile loadProfile;

  @Mock private ResolveURLUseCase resolveURLUseCase;

  @InjectMocks private RetrieveProfileService retrieveProfileService;

  @Test
  @DisplayName("Scenario: 성공 - 프로필 이미지가 없는 사용자 정보를 조회하면 URL이 null인 정보를 반환한다")
  void retrieve_profile_without_profile_image() {
    // Given
    UUID userId = UUID.randomUUID();
    RetrieveProfileCmd cmd = new RetrieveProfileCmd(userId);
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
    ProfileLoaded loadedUser =
        new ProfileLoaded(userId, "test@example.com", "Test User", Role.USER, now, null);

    given(loadProfile.operate(userId)).willReturn(loadedUser);

    // When
    RetrieveProfile result = retrieveProfileService.operate(cmd);

    // Then
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.profileImageUrl()).isNull();
    verify(loadProfile).operate(userId);
  }

  @Test
  @DisplayName("Scenario: 성공 - 프로필 이미지가 있는 사용자 정보를 조회하면 변환된 URL을 포함한 정보를 반환한다")
  void retrieve_profile_with_profile_image() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID imageId = UUID.randomUUID();
    String expectedUrl = "http://example.com/files/" + imageId;
    RetrieveProfileCmd cmd = new RetrieveProfileCmd(userId);
    java.time.LocalDateTime now = java.time.LocalDateTime.now();
    ProfileLoaded loadedUser =
        new ProfileLoaded(userId, "test@example.com", "Test User", Role.USER, now, imageId);

    given(loadProfile.operate(userId)).willReturn(loadedUser);
    given(resolveURLUseCase.operate(imageId)).willReturn(expectedUrl);

    // When
    RetrieveProfile result = retrieveProfileService.operate(cmd);

    // Then
    assertThat(result.id()).isEqualTo(userId);
    assertThat(result.profileImageId()).isEqualTo(imageId);
    assertThat(result.profileImageUrl()).isEqualTo(expectedUrl);
    verify(loadProfile).operate(userId);
    verify(resolveURLUseCase).operate(imageId);
  }
}
