package io.dough.api.useCases.auth.authenticate.adapters.out.persistence;

import io.dough.api.useCases.auth.authenticate.application.port.out.AuthenticatableUser;
import io.dough.api.useCases.auth.authenticate.application.port.out.LoadAuthenticatableUser;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadAuthUserPersistAdapter implements LoadAuthenticatableUser {

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
