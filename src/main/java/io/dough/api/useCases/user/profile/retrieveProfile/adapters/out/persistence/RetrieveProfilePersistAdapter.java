package io.dough.api.useCases.user.profile.retrieveProfile.adapters.out.persistence;

import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.user.profile.retrieveProfile.application.port.out.LoadProfile;
import io.dough.api.useCases.user.profile.retrieveProfile.application.port.out.ProfileLoaded;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RetrieveProfilePersistAdapter implements LoadProfile {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public ProfileLoaded operate(UUID userId) {
    UserEntity entity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new ProfileLoaded(
        entity.getId(),
        entity.getEmail(),
        entity.getDisplayName(),
        entity.getRole(),
        entity.getCreatedAt(),
        entity.getProfileImage() != null ? entity.getProfileImage().getId() : null);
  }
}
