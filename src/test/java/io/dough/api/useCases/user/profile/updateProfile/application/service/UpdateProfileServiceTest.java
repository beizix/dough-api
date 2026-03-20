package io.dough.api.useCases.user.profile.updateProfile.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.user.profile.updateProfile.application.port.in.ProfileUpdated;
import io.dough.api.useCases.user.profile.updateProfile.application.port.in.UpdateProfileCmd;
import io.dough.api.useCases.user.profile.updateProfile.application.port.out.UpdateProfile;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateProfileServiceTest {

  @Mock private UpdateProfile updateProfile;

  @InjectMocks private UpdateProfileService updateProfileService;

  @Test
  @DisplayName("Scenario: 성공 - 사용자 프로필이 성공적으로 업데이트된다")
  void update_user_profile_success() {
    // Given
    UUID userId = UUID.randomUUID();
    String newEmail = "new.user@example.com";
    String newDisplayName = "New User Name";
    UpdateProfileCmd cmd = new UpdateProfileCmd(userId, newEmail, newDisplayName);
    LocalDateTime now = LocalDateTime.now();
    ProfileUpdated expectedProfile = new ProfileUpdated(newEmail, newDisplayName, now);

    given(updateProfile.operate(any(UpdateProfileCmd.class))).willReturn(expectedProfile);

    // When
    ProfileUpdated result = updateProfileService.operate(cmd);

    // Then
    assertThat(result).isEqualTo(expectedProfile);
    verify(updateProfile).operate(cmd);
  }
}
