package io.vision.api.useCases.storage.getFileURL.application.domain.model;

import io.vision.api.useCases.storage.saveFile.application.domain.model.FileUploadType;

public record GetFileURL(FileUploadType fileUploadType, String path, String filename) {}
