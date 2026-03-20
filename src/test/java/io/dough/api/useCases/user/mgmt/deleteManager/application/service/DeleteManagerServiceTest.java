package io.dough.api.useCases.user.mgmt.deleteManager.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.DeleteManagerCmd;
import io.dough.api.useCases.user.mgmt.deleteManager.application.port.in.ManagerDeleted;
import io.dough.api.useCases.user.mgmt.deleteManager.application.port.out.DeleteManager;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteManagerServiceTest {

  @Mock private DeleteManager deleteManager;

  @InjectMocks private DeleteManagerService deleteManagerService;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 삭제 시 영속성 포트가 호출되고 결과가 반환된다")
  void delete_manager_success() {
    // Given
    UUID managerId = UUID.randomUUID();
    DeleteManagerCmd cmd = new DeleteManagerCmd(managerId, "admin@dough.io");
    ManagerDeleted expected = new ManagerDeleted(true, LocalDateTime.now());
    given(deleteManager.operate(cmd)).willReturn(expected);

    // When
    ManagerDeleted actual = deleteManagerService.operate(cmd);

    // Then
    assertThat(actual).isEqualTo(expected);
    verify(deleteManager).operate(cmd);
  }
}
