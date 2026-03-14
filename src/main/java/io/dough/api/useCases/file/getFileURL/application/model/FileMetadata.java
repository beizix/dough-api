package io.dough.api.useCases.file.getFileURL.application.model;

import io.dough.api.useCases.shared.domain.file.FileUploadType;

public record FileMetadata(FileUploadType fileUploadType, String path, String filename) {}
