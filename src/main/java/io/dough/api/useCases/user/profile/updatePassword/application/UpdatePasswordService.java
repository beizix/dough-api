package io.dough.api.useCases.user.profile.updatePassword.application;

import io.dough.api.useCases.user.profile.updatePassword.domain.model.UpdatePassword;
import io.dough.api.useCases.user.profile.updatePassword.domain.model.UpdatePasswordCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class UpdatePasswordService implements UpdatePasswordUseCase {

  private final GetUser getUser;
  private final SaveUser saveUser;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void operate(UpdatePasswordCmd command) {
    // 1. 신규 패스워드 일치 확인 (UpdatePasswordCmd 생성 시 이미 완료됨)

    // 2. 현재 사용자 정보 조회 (userId를 전달하여 영속성 계층에 위임)
    UpdatePassword currentPasswordModel = getUser.operate(command.userId());

    // 3. 현재 패스워드 검증 (도메인 모델에 위임)
    currentPasswordModel.verify(command.currentPassword(), passwordEncoder);

    // 4. 새로운 패스워드로 도메인 모델 생성 (도메인 모델에 위임)
    UpdatePassword updatedPasswordModel = currentPasswordModel.update(command.newPassword(), passwordEncoder);

    // 5. 저장 요청
    saveUser.operate(updatedPasswordModel);
  }
}
