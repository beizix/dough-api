package io.dough.api.useCases.user.getMyProfile.application;

import io.dough.api.useCases.user.getMyProfile.application.domain.model.MyProfileLoaded;
import java.util.UUID;

public interface LoadMyProfile {
  MyProfileLoaded operate(UUID userId);
}
