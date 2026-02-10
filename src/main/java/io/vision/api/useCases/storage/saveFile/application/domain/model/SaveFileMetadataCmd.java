package io.vision.api.useCases.storage.saveFile.application.domain.model;

public record SaveFileMetadataCmd(
    FileUploadType type, String path, String name, String originName, Long fileLength) {}
