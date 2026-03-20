package io.dough.api.useCases.user.profile.updateProfileImage.application.port.in;

import java.util.UUID;

public record ProfileImageUpdated(
    UUID id, String name, String originName, long fileLength, String referURL) {}
