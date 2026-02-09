package io.vision.api.useCases.storage.getFileResource.application.model;

import io.vision.api.useCases.storage.uploadFile.application.model.FileUploadType;

public record GetFileResource(FileUploadType fileUploadType, String path, String filename) {}
