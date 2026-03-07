package io.dough.api.useCases.user.maintenance.getUser.adapters.persistence;

import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.maintenance.getUser.application.LoadUser;
import io.dough.api.useCases.user.maintenance.getUser.domain.GetUserCmd;
import io.dough.api.useCases.user.maintenance.getUser.domain.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserDetailPersistAdapter implements LoadUser {

  private final UserRepository userRepository;

  @Override
  public UserDetail operate(GetUserCmd cmd) {
    UserEntity user =
        userRepository
            .findById(cmd.id())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    return new UserDetail(
        user.getId(), user.getEmail(), user.getDisplayName(), user.getRole(), user.getCreatedAt());
  }
}
