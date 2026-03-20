package io.dough.api.useCases.file.upload.application.port.in;

import io.dough.api.useCases.shared.domain.file.FileUploadType;
import java.io.InputStream;

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
}

