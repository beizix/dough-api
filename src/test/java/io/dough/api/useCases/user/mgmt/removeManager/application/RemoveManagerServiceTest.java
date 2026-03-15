package io.dough.api.useCases.user.mgmt.removeManager.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import io.dough.api.useCases.user.mgmt.removeManager.application.model.ManagerRemoved;
import io.dough.api.useCases.user.mgmt.removeManager.application.model.RemoveManagerCmd;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveManagerServiceTest {

  @Mock private DeleteManager deleteManager;

  @InjectMocks private RemoveManagerService removeManagerService;

  @Test
  @DisplayName("Scenario: 성공 - 매니저 삭제 시 영속성 포트가 호출되고 결과가 반환된다")
  void remove_manager_success() {
    // Given
    UUID managerId = UUID.randomUUID();
    RemoveManagerCmd cmd = new RemoveManagerCmd(managerId, "admin@dough.io");
    ManagerRemoved expected = new ManagerRemoved(true, LocalDateTime.now());
    given(deleteManager.operate(cmd)).willReturn(expected);

    // When
    ManagerRemoved actual = removeManagerService.operate(cmd);

    // Then
    assertThat(actual).isEqualTo(expected);
    verify(deleteManager).operate(cmd);
  }
}
