package io.dough.api.useCases.user.profile.updatePassword.adapters.out.persistence;

import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.user.profile.updatePassword.application.port.out.LoadPassword;
import io.dough.api.useCases.user.profile.updatePassword.application.port.out.Password;
import io.dough.api.useCases.user.profile.updatePassword.application.port.out.SavePassword;
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

    userEntity.setPassword(password.encodedValue());
    userRepository.save(userEntity);
  }
}
