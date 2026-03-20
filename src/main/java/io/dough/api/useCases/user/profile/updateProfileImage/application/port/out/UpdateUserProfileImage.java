package io.dough.api.useCases.user.profile.updateProfileImage.application.port.out;

import java.util.UUID;

public interface UpdateUserProfileImage {
  void operate(UUID userId, UUID fileId);
}
