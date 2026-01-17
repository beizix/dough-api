package io.vision.api.useCases.auth.application.model;

import io.vision.api.common.application.enums.Role;
import java.util.List;

public record AuthCmd(String email, List<Role> roles) {}
