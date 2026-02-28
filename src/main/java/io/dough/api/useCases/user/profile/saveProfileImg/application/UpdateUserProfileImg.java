package io.dough.api.useCases.user.profile.saveProfileImg.application;

import java.util.UUID;

public interface UpdateUserProfileImg {
  void operate(UUID userId, UUID fileId);
}
