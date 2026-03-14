package io.dough.api.useCases.user.signup.adapters.persistence;

import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.signup.application.RegisterUser;
import io.dough.api.useCases.user.signup.application.model.SignupUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterUserPersistAdapter implements RegisterUser {

  private final UserRepository userRepository;

  @Override
  public boolean existsByEmailAndRole(String email, Role role) {
    return userRepository.existsByEmailAndRole(email, role);
  }

  @Override
  public SignupUser save(String email, String password, String displayName, Role role) {
    // refreshToken 은 추후 토큰 발급 과정에서 업데이트 됩니다.
    UserEntity entity = new UserEntity(email, password, displayName, role, null);
    UserEntity saved = userRepository.save(entity);
    return new SignupUser(
        saved.getId(),
        saved.getEmail(),
        saved.getPassword(),
        saved.getDisplayName(),
        saved.getRole());
  }
}
