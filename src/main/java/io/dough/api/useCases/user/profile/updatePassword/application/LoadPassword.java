package io.dough.api.useCases.user.profile.updatePassword.application;

import java.util.UUID;

import io.dough.api.useCases.user.profile.updatePassword.application.model.Password;

public interface LoadPassword {
  Password operate(UUID userId);
}
