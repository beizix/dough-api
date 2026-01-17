package io.vision.api.useCases.login.application;

import io.vision.api.useCases.auth.application.model.AuthToken;
import io.vision.api.useCases.login.application.model.LoginCmd;

public interface LoginUseCase {
  AuthToken operate(LoginCmd cmd);
}
