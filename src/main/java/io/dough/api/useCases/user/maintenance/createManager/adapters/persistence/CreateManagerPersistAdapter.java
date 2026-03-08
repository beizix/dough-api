package io.dough.api.useCases.user.maintenance.createManager.adapters.persistence;

import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.maintenance.createManager.application.SaveManager;
import io.dough.api.useCases.user.maintenance.createManager.domain.CreateManagerCmd;
import io.dough.api.useCases.user.maintenance.createManager.domain.ManagerCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateManagerPersistAdapter implements SaveManager {

  private final UserRepository userRepository;

  @Override
  public ManagerCreated operate(CreateManagerCmd cmd) {
    // 전달받은 (암호화된) 비밀번호를 사용하여 사용자 엔티티 생성
    UserEntity user =
        new UserEntity(cmd.email(), cmd.password(), cmd.displayName(), cmd.role(), null);

    userRepository.save(user);

    return new ManagerCreated(
        user.getId(), user.getEmail(), user.getDisplayName(), user.getRole(), user.getCreatedAt());
  }
}
