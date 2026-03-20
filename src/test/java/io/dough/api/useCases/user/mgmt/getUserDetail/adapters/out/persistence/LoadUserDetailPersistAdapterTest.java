package io.dough.api.useCases.user.mgmt.getUserDetail.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.GetUserDetailCmd;
import io.dough.api.useCases.user.mgmt.getUserDetail.application.port.in.UserDetailLoaded;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(LoadUserDetailPersistAdapter.class)
class LoadUserDetailPersistAdapterTest extends DataJpaTestBase {

  @Autowired private UserRepository userRepository;

  @Autowired private LoadUserDetailPersistAdapter loadUserDetailPersistAdapter;

  private UUID savedUserId;

  @BeforeEach
  void setUp() {
    UserEntity user = new UserEntity("test@dough.io", "pass", "테스터", Role.USER, null);
    userRepository.save(user);
    savedUserId = user.getId();
  }

  @Test
  @DisplayName("Scenario: 성공 - 존재하는 사용자 ID로 조회 시 상세 정보를 반환한다")
  void get_user_detail_success() {
    // Given
    GetUserDetailCmd cmd = new GetUserDetailCmd(savedUserId);

    // When
    UserDetailLoaded result = loadUserDetailPersistAdapter.operate(cmd);

    // Then
    assertThat(result.id()).isEqualTo(savedUserId);
    assertThat(result.email()).isEqualTo("test@dough.io");
    assertThat(result.displayName()).isEqualTo("테스터");
    assertThat(result.role()).isEqualTo(Role.USER);
    assertThat(result.createdAt()).isNotNull();
  }

  @Test
  @DisplayName("Scenario: 실패 - 존재하지 않는 사용자 ID로 조회 시 예외가 발생한다")
  void get_user_detail_fail_not_found() {
    // Given
    GetUserDetailCmd cmd = new GetUserDetailCmd(UUID.randomUUID());

    // Then
    assertThatThrownBy(() -> loadUserDetailPersistAdapter.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
