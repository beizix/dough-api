package io.dough.api.useCases.user.mgmt.deleteManager.adapters.persistence;

import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.deleteManager.application.DeleteManager;
import io.dough.api.useCases.user.mgmt.deleteManager.application.model.DeleteManagerCmd;
import io.dough.api.useCases.user.mgmt.deleteManager.application.model.ManagerDeleted;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteManagerPersistAdapter implements DeleteManager {

  private final UserRepository userRepository;

  @Override
  public ManagerDeleted operate(DeleteManagerCmd cmd) {
    UserEntity user =
        userRepository
            .findById(cmd.id())
            .filter(u -> u.getRole() == Role.MANAGER)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));

    user.setDeleted(true);
    user.setDeletedAt(LocalDateTime.now());
    user.setDeletedBy(cmd.deletedBy());
    userRepository.save(user);

    return new ManagerDeleted(true, user.getDeletedAt());
  }
}
