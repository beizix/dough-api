package io.dough.api.useCases.auth.logout.application;

import io.dough.api.useCases.auth.logout.domain.LogoutCmd;

/** 로그아웃 기능을 정의한 입력 포트입니다. */
public interface LogoutUseCase {
  /**
   * 로그아웃 처리를 수행합니다.
   *
   * @param cmd 로그아웃 요청 커맨드
   */
  void operate(LogoutCmd cmd);
}
