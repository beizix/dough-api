package io.vision.api.useCases.uploadFile.application;

import io.vision.api.useCases.uploadFile.application.model.FileStorageType;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface SaveToFileStoragePortOut {
  FileStorageType getStorageType();

  void operate(MultipartFile multipartFile, String createSubPath, String createFilename)
      throws IOException;
}
