package io.dough.api.useCases.user.profile.retrieveProfile.application.port.out;

import java.util.UUID;

public interface LoadProfile {
  ProfileLoaded operate(UUID userId);
}
