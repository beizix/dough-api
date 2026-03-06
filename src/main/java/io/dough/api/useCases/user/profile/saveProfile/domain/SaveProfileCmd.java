package io.dough.api.useCases.user.profile.saveProfile.domain;

import java.util.UUID;

public record SaveProfileCmd(UUID loginUserId, String email, String displayName) {}
