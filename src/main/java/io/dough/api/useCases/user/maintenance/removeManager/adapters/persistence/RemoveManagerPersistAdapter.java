package io.dough.api.useCases.user.maintenance.removeManager.adapters.persistence;

import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.removeManager.application.DeleteManager;
import io.dough.api.useCases.user.maintenance.removeManager.application.model.ManagerRemoved;
import io.dough.api.useCases.user.maintenance.removeManager.application.model.RemoveManagerCmd;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveManagerPersistAdapter implements DeleteManager {

  private final UserRepository userRepository;

  @Override
  public ManagerRemoved operate(RemoveManagerCmd cmd) {
    UserEntity user =
        userRepository
            .findById(cmd.id())
            .filter(u -> u.getRole() == Role.MANAGER)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));

    user.setDeleted(true);
    user.setDeletedAt(LocalDateTime.now());
    user.setDeletedBy(cmd.removedBy());
    userRepository.save(user);

    return new ManagerRemoved(true, user.getDeletedAt());
  }
}
