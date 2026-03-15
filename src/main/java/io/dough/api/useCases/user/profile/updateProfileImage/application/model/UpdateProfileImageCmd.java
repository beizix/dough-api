package io.dough.api.useCases.user.profile.updateProfileImage.application.model;

import java.io.InputStream;
import java.util.UUID;

public record UpdateProfileImageCmd(
    UUID userId, InputStream inputStream, String originalFilename, long fileSize) {}
