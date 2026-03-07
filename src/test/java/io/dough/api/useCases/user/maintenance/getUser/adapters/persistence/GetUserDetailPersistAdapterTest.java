package io.dough.api.useCases.user.maintenance.getUser.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.getUser.domain.GetUserCmd;
import io.dough.api.useCases.user.maintenance.getUser.domain.UserDetail;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(GetUserDetailPersistAdapter.class)
class GetUserDetailPersistAdapterTest extends DataJpaTestBase {

  @Autowired private UserRepository userRepository;

  @Autowired private GetUserDetailPersistAdapter getUserDetailPersistAdapter;

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
    GetUserCmd cmd = new GetUserCmd(savedUserId);

    // When
    UserDetail result = getUserDetailPersistAdapter.operate(cmd);

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
    GetUserCmd cmd = new GetUserCmd(UUID.randomUUID());

    // Then
    assertThatThrownBy(() -> getUserDetailPersistAdapter.operate(cmd))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
