package io.dough.api.useCases.file.resolveURL.application;

import io.dough.api.useCases.file.resolveURL.application.model.FileMetadataLoaded;
import io.dough.api.useCases.shared.domain.file.FileStorageType;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResolveURLService implements ResolveURLUseCase {
  private final LoadFileMetadata loadFileMetadata;
  private final Set<ProvideFileURL> getResourceURLStrategies;

  @Override
  public String operate(UUID fileUuid) {
    FileMetadataLoaded loaded = loadFileMetadata.operate(fileUuid);
    return getResourceURLStrategy(loaded.fileUploadType().getFileStorageType())
        .operate(loaded.path(), loaded.filename());
  }

  private ProvideFileURL getResourceURLStrategy(FileStorageType fileStorageType) {
    return getResourceURLStrategies.stream()
        .filter(provideFileURL -> provideFileURL.getStorageType().equals(fileStorageType))
        .findFirst()
        .orElseThrow(
            () ->
                new NoSuchElementException(
                    String.format("No resource url strategy found: %s", fileStorageType.name())));
  }
}
