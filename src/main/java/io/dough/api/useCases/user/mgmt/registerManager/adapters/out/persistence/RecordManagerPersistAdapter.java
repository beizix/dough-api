package io.dough.api.useCases.user.mgmt.registerManager.adapters.out.persistence;

import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.registerManager.application.port.in.ManagerRegistered;
import io.dough.api.useCases.user.mgmt.registerManager.application.port.in.RegisterManagerCmd;
import io.dough.api.useCases.user.mgmt.registerManager.application.port.out.RecordManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecordManagerPersistAdapter implements RecordManager {

  private final UserRepository userRepository;

  @Override
  public boolean existsByEmailAndRole(String email, Role role) {
    return userRepository.existsByEmailAndRole(email, role);
  }

  @Override
  public ManagerRegistered operate(RegisterManagerCmd cmd) {
    // 전달받은 (암호화된) 비밀번호를 사용하여 사용자 엔티티 생성
    UserEntity user =
        new UserEntity(cmd.email(), cmd.password(), cmd.displayName(), cmd.role(), null);

    userRepository.save(user);

    return new ManagerRegistered(
        user.getId(), user.getEmail(), user.getDisplayName(), user.getRole(), user.getCreatedAt());
  }
}
