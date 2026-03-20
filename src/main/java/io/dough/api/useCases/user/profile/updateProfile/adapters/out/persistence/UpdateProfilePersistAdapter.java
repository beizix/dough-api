package io.dough.api.useCases.user.profile.updateProfile.adapters.out.persistence;

import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.user.profile.updateProfile.application.port.in.ProfileUpdated;
import io.dough.api.useCases.user.profile.updateProfile.application.port.in.UpdateProfileCmd;
import io.dough.api.useCases.user.profile.updateProfile.application.port.out.UpdateProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
class UpdateProfilePersistAdapter implements UpdateProfile {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public ProfileUpdated operate(UpdateProfileCmd cmd) {
    UserEntity userEntity =
        userRepository
            .findById(cmd.loginUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found for update"));

    userEntity.setEmail(cmd.email());
    userEntity.setDisplayName(cmd.displayName());

    UserEntity savedUser = userRepository.save(userEntity);

    return new ProfileUpdated(
        savedUser.getEmail(), savedUser.getDisplayName(), savedUser.getUpdatedAt());
  }
}
