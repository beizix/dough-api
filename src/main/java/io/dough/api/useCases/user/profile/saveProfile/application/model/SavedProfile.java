package io.dough.api.useCases.user.profile.saveProfile.application.model;

import java.time.LocalDateTime;

public record SavedProfile(String email, String displayName, LocalDateTime updatedAt) {}
