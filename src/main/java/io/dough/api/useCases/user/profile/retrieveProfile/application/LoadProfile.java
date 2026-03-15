package io.dough.api.useCases.user.profile.retrieveProfile.application;

import io.dough.api.useCases.user.profile.retrieveProfile.application.model.ProfileLoaded;
import java.util.UUID;

public interface LoadProfile {
  ProfileLoaded operate(UUID userId);
}
