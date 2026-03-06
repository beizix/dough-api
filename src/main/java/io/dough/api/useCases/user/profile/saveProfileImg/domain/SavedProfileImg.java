package io.dough.api.useCases.user.profile.saveProfileImg.domain;

import java.util.UUID;

public record SavedProfileImg(
    UUID id, String name, String originName, Long fileLength, String referURL) {}
