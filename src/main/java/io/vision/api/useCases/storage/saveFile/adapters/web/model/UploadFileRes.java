package io.vision.api.useCases.storage.saveFile.adapters.web.model;

import java.util.UUID;

public record UploadFileRes(
    UUID id, String path, String name, String originName, String referURL) {}
