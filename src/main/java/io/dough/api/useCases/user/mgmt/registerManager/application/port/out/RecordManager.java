package io.dough.api.useCases.user.mgmt.registerManager.application.port.out;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.registerManager.application.port.in.ManagerRegistered;
import io.dough.api.useCases.user.mgmt.registerManager.application.port.in.RegisterManagerCmd;

public interface RecordManager {
  /**
   * 해당 이메일과 권한을 가진 사용자가 이미 존재하는지 확인합니다.
   *
   * @param email 이메일
   * @param role 권한
   * @return 존재 여부 (true: 존재함, false: 존재하지 않음)
   */
  boolean existsByEmailAndRole(String email, Role role);

  ManagerRegistered operate(RegisterManagerCmd cmd);
}
