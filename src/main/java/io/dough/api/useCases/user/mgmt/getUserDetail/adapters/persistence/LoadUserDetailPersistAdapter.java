package io.dough.api.useCases.user.mgmt.getUserDetail.adapters.persistence;

import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.LoadUserDetail;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.model.GetUserDetailCmd;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.model.UserDetailLoaded;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadUserDetailPersistAdapter implements LoadUserDetail {

  private final UserRepository userRepository;

  @Override
  public UserDetailLoaded operate(GetUserDetailCmd cmd) {
    UserEntity user =
        userRepository
            .findById(cmd.id())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    return new UserDetailLoaded(
        user.getId(), user.getEmail(), user.getDisplayName(), user.getRole(), user.getCreatedAt());
  }
}
