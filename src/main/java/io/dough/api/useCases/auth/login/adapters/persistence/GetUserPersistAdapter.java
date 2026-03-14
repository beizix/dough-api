package io.dough.api.useCases.auth.login.adapters.persistence;

import io.dough.api.useCases.auth.login.application.GetUser;
import io.dough.api.useCases.auth.login.domain.AuthenticatableUser;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserPersistAdapter implements GetUser {

  private final UserRepository userRepository;

  @Override
  public Optional<AuthenticatableUser> operate(String email, Role role) {
    return userRepository
        .findByEmailAndRole(email, role)
        .map(
            entity ->
                new AuthenticatableUser(
                    entity.getId(),
                    entity.getEmail(),
                    entity.getPassword(),
                    entity.getDisplayName(),
                    entity.getRole()));
  }
}
