package io.dough.api.useCases.user.profile.updatePassword.adapters.persistence;

import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.profile.updatePassword.application.LoadPassword;
import io.dough.api.useCases.user.profile.updatePassword.application.SavePassword;
import io.dough.api.useCases.user.profile.updatePassword.domain.Password;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UpdatePasswordPersistAdapter implements LoadPassword, SavePassword {

  private final UserRepository userRepository;

  @Override
  public Password operate(UUID userId) {
    UserEntity userEntity =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new Password(userEntity.getId(), userEntity.getPassword());
  }

  @Override
  public void operate(Password password) {
    UserEntity userEntity =
        userRepository
            .findById(password.id())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    userEntity.setPassword(password.encodedPassword());
    userRepository.save(userEntity);
  }
}
