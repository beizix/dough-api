package io.vision.api.useCases.auth.application.model;

import io.vision.api.common.application.enums.Role;

public record CreateTokenCmd(String email, String displayName, Role role) {
}
