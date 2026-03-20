package io.dough.api.useCases.file.resolveURL.application.port.in;

import io.dough.api.useCases.shared.domain.file.FileUploadType;

public record FileMetadataLoaded(FileUploadType fileUploadType, String path, String filename) {}
