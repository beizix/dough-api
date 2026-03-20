package io.dough.api.useCases.file.upload.adapters.in.web.model;

import java.util.UUID;

public record UploadFileResponse(
    UUID id, String path, String name, String originName, String referURL) {}
