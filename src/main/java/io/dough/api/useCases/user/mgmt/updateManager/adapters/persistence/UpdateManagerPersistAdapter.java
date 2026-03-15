package io.dough.api.useCases.user.mgmt.updateManager.adapters.persistence;

import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.updateManager.application.UpdateManagerPortOut;
import io.dough.api.useCases.user.mgmt.updateManager.application.model.ManagerUpdated;
import io.dough.api.useCases.user.mgmt.updateManager.application.model.UpdateManagerCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateManagerPersistAdapter implements UpdateManagerPortOut {

  private final UserRepository userRepository;

  @Override
  public ManagerUpdated operate(UpdateManagerCmd cmd) {
    UserEntity user =
        userRepository
            .findByEmailAndRole(cmd.email(), Role.MANAGER)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));

    if (cmd.displayName() != null) {
      user.setDisplayName(cmd.displayName());
    }
    if (cmd.password() != null) {
      user.setPassword(cmd.password());
    }

    userRepository.save(user);

    return new ManagerUpdated(
        user.getId(), user.getEmail(), user.getDisplayName(), user.getRole(), user.getUpdatedAt());
  }
}
