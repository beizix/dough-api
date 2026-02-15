package io.dough.api.useCases.user.getMyProfile.adapters.persistence;

import io.dough.api.common.adapters.persistence.entity.UserEntity;
import io.dough.api.common.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.getMyProfile.application.LoadMyProfile;
import io.dough.api.useCases.user.getMyProfile.application.domain.model.MyProfileLoaded;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoadMyProfilePersistAdapter implements LoadMyProfile {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public MyProfileLoaded operate(UUID userId) {
    UserEntity entity =
      userRepository
        .findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new MyProfileLoaded(
      entity.getId(),
      entity.getEmail(),
      entity.getDisplayName(),
      entity.getRole(),
      entity.getCreatedAt(),
      entity.getProfileImage() != null ? entity.getProfileImage().getId() : null);
  }
}
