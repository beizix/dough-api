package io.dough.api.config.init;

import io.dough.api.useCases.shared.domain.auth.Role;
import io.dough.api.useCases.user.maintenance.createManager.application.CreateManagerUseCase;
import io.dough.api.useCases.user.maintenance.createManager.domain.CreateManagerCmd;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.init.data", havingValue = "true")
public class InitialManagerDataConfig implements CommandLineRunner {

  private final CreateManagerUseCase createManagerUseCase;

  @Override
  public void run(String... args) {
    log.info("✦ Initializing manager data: Creating Super Manager account...");

    try {
      CreateManagerCmd managerCmd =
          new CreateManagerCmd("manager@dough.io", "SuperManager", "manager1@#$", Role.MANAGER);

      createManagerUseCase.operate(managerCmd);
      log.info("✦ Super Manager account created successfully.");
    } catch (Exception e) {
      log.warn("✦ Super Manager account initialization skipped: {}", e.getMessage());
    }
  }
}
