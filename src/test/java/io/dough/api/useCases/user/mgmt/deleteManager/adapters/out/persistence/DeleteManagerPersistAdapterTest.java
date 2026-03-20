package io.dough.api.useCases.user.mgmt.deleteManager.adapters.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.UserEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.UserRepository;
import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.DeleteManagerCmd;
import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.ManagerDeleted;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(DeleteManagerPersistAdapter.class)
class DeleteManagerPersistAdapterTest extends DataJpaTestBase {

  @Autowired private UserRepository userRepository;

  @Autowired private DeleteManagerPersistAdapter deleteManagerPersistAdapter;

  @PersistenceContext private EntityManager entityManager;

  private UUID savedManagerId;

  @BeforeEach
  void setUp() {
    UserEntity manager = new UserEntity("manager@dough.io", "pass123!", "매니저", Role.MANAGER, null);
    userRepository.save(manager);
    savedManagerId = manager.getId();
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("Scenario: 성공 - 매니저 삭제 시 Soft Delete 필드(deletedAt, deletedBy)가 정상 설정된다")
  void delete_manager_success() {
    // Given
    DeleteManagerCmd cmd = new DeleteManagerCmd(savedManagerId, "admin@dough.io");

    // When
    ManagerDeleted result = deleteManagerPersistAdapter.operate(cmd);

    // Then
    assertThat(result.deleted()).isTrue();
    assertThat(result.deletedAt()).isNotNull();

    entityManager.flush();
    entityManager.clear();

    // 1. @SQLRestriction에 의해 findById로는 조회되지 않아야 함
    assertThat(userRepository.findById(savedManagerId)).isEmpty();

    // 2. 네이티브 쿼리를 통해 @SQLRestriction을 우회하여 Soft Delete 상태 검증
    Object[] row =
        (Object[])
            entityManager
                .createNativeQuery(
                    "SELECT deleted, deleted_at, deleted_by FROM users WHERE id = :id")
                .setParameter("id", savedManagerId)
                .getSingleResult();

    assertThat((Boolean) row[0]).isTrue();
    assertThat(row[1]).isNotNull();
    assertThat((String) row[2]).isEqualTo("admin@dough.io");
  }
}
