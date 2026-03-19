package io.dough.api.useCases.auth.logout.adapters.persistence;

import io.dough.api.useCases.auth.logout.application.InvalidateRefreshToken;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvalateTokenPersistAdapter implements InvalidateRefreshToken {

  private final UserRepository userRepository;

  @Override
  public void operate(UUID userId) {
    userRepository
        .findById(userId)
        .ifPresent(
            user -> {
              user.setRefreshToken(null);
              userRepository.save(user);
            });
  }
}
