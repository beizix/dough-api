package io.dough.api.useCases.auth.logout.application.service;

import io.dough.api.useCases.auth.logout.application.port.in.LogoutCmd;
import io.dough.api.useCases.auth.logout.application.port.in.LogoutUseCase;
import io.dough.api.useCases.auth.logout.application.port.out.InvalidateRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutUseCase {

  private final InvalidateRefreshToken invalidateRefreshToken;

  @Override
  @Transactional
  public void operate(LogoutCmd cmd) {
    invalidateRefreshToken.operate(cmd.userId());
  }
}
