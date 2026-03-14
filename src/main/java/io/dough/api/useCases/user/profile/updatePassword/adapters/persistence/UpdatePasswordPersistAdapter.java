package io.dough.api.useCases.user.profile.updatePassword.adapters.persistence;

import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.profile.updatePassword.application.GetUser;
import io.dough.api.useCases.user.profile.updatePassword.application.SaveUser;
import io.dough.api.useCases.user.profile.updatePassword.domain.UpdatedPassword;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UpdatePasswordPersistAdapter implements GetUser, SaveUser {

  private final UserRepository userRepository;

  @Override
  public UpdatedPassword operate(UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new UpdatedPassword(userEntity.getId(), userEntity.getPassword());
  }

  @Override
  public void operate(UpdatedPassword updatedPassword) {
    UserEntity userEntity =
        userRepository
            .findById(updatedPassword.id())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    userEntity.setPassword(updatedPassword.encodedPassword());
    userRepository.save(userEntity);
  }
}
