package io.dough.api.useCases.file.getFileURL.application.domain.model;

import io.dough.api.useCases.file.saveFile.application.domain.model.FileUploadType;

public record GetFileURL(FileUploadType fileUploadType, String path, String filename) {}
