package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.application.model.UpdatePasswordCmd;
import io.dough.api.useCases.user.profile.updatePassword.domain.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UpdatePasswordService implements UpdatePasswordUseCase {

  private final LoadPassword loadPassword;
  private final SavePassword savePassword;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void operate(UpdatePasswordCmd command) {
    // 1. 현재 패스워드 정보 조회
    Password currentPassword = loadPassword.operate(command.userId());

    // 2. 현재 패스워드 검증
    currentPassword.verify(command.currentPassword(), passwordEncoder);

    // 3. 새로운 패스워드로 업데이트 (해싱 포함)
    Password updatedPassword = currentPassword.update(command.newPassword(), passwordEncoder);

    // 4. 변경 내용 저장
    savePassword.operate(updatedPassword);
  }
}
