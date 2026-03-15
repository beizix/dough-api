package io.dough.api.useCases.user.profile.updateProfileImage.application.model;

import java.util.UUID;

public record ProfileImageUpdated(
    UUID id, String name, String originName, Long fileLength, String referURL) {}
