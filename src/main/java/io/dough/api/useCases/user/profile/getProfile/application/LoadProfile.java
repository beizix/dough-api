package io.dough.api.useCases.user.profile.getProfile.application;

import io.dough.api.useCases.user.profile.getProfile.application.domain.model.ProfileLoaded;
import java.util.UUID;

public interface LoadProfile {
  ProfileLoaded operate(UUID userId);
}
