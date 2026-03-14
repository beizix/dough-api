package io.dough.api.useCases.file.getFileURL.application;

import io.dough.api.useCases.file.getFileURL.application.model.FileMetadata;
import io.dough.api.useCases.shared.domain.file.FileStorageType;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFileURLService implements GetFileURLUseCase {
  private final GetFileMetadata getFileMetadata;
  private final Set<GetFileURL> getResourceURLStrategies;

  @Override
  public String operate(UUID fileUuid) {
    FileMetadata fileMetadata = getFileMetadata.operate(fileUuid);
    return getResourceURLStrategy(fileMetadata.fileUploadType().getFileStorageType())
        .operate(fileMetadata.path(), fileMetadata.filename());
  }

  private GetFileURL getResourceURLStrategy(FileStorageType fileStorageType) {
    return getResourceURLStrategies.stream()
        .filter(getFileURL -> getFileURL.getStorageType().equals(fileStorageType))
        .findFirst()
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    String.format("No resource url strategy found: %s", fileStorageType.name())));
  }
}
