package io.dough.api.useCases.user.mgmt.getUserDetail.adapters.out.persistence;

import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.out.LoadUserDetail;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.GetUserDetailCmd;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.UserDetailLoaded;
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
