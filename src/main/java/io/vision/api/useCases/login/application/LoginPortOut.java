package io.vision.api.useCases.login.application;

import io.vision.api.useCases.login.application.model.LoginUser;
import java.util.Optional;

public interface LoginPortOut {
    Optional<LoginUser> loadUser(String email);
}
