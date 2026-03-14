package io.dough.api.useCases.file.upload.application.model;

import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

import io.dough.api.useCases.shared.domain.file.FileUploadType;

public record UploadFileCmd(
    FileUploadType fileUploadType,
    InputStream inputStream,
    String originalFilename,
    long fileSize) {

  public UploadFileCmd {
    if (inputStream == null || originalFilename == null || originalFilename.isEmpty()) {
      throw new IllegalArgumentException("exception.file.invalid_input");
    }
  }

  public Set<String> getAllowedExtensions() {
    return fileUploadType.getAcceptableFileTypes().stream()
        .flatMap(type -> type.getExtensions().stream())
        .collect(Collectors.toSet());
  }

  public Set<String> getAllowedMimeTypes() {
    return fileUploadType.getAcceptableFileTypes().stream()
        .flatMap(type -> type.getMimeTypes().stream())
        .collect(Collectors.toSet());
  }
}
