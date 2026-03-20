package io.dough.api.useCases.user.profile.updateProfile.application.port.in;

import java.time.LocalDateTime;

public record ProfileUpdated(String email, String displayName, LocalDateTime updatedAt) {}
