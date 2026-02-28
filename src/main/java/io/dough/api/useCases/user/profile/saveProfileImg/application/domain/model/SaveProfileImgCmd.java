package io.dough.api.useCases.user.profile.saveProfileImg.application.domain.model;

import java.io.InputStream;
import java.util.UUID;

public record SaveProfileImgCmd(
    UUID userId, InputStream inputStream, String originalFilename, long fileSize) {}
