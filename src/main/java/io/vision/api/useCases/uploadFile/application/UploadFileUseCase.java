package io.vision.api.useCases.uploadFile.application;

import io.vision.api.useCases.uploadFile.application.model.FileUploadType;
import io.vision.api.useCases.uploadFile.application.model.SaveToFileStorage;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileUseCase {
  Optional<SaveToFileStorage> operate(FileUploadType fileUploadType, MultipartFile file);

  Optional<SaveToFileStorage> operate(FileUploadType fileUploadType, String base64DataStr);
}
