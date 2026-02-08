package io.vision.api.useCases.uploadFile.application;

import io.vision.api.useCases.uploadFile.application.model.FileUploadType;
import io.vision.api.useCases.uploadFile.application.model.UploadFile;
import java.io.InputStream;
import java.util.Optional;

public interface UploadFileUseCase {
  Optional<UploadFile> operate(
      FileUploadType fileUploadType, InputStream inputStream, String originalFilename, long fileSize);
}
