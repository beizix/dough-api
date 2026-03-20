package io.dough.api.useCases.user.profile.updatePassword.application.model;

import java.util.UUID;

public record Password(UUID id, String encodedValue) {
}
