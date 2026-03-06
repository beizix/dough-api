package io.dough.api.useCases.file.getFileURL.adapters.persistence;

import io.dough.api.common.adapters.persistence.entity.FileMetadataEntity;
import io.dough.api.common.adapters.persistence.repository.FileMetadataRepository;
import io.dough.api.useCases.file.getFileURL.application.GetFileMetadata;
import io.dough.api.useCases.file.getFileURL.application.model.FileMetadata;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetFileMetadataPersistAdapter implements GetFileMetadata {
  private final FileMetadataRepository fileMetadataRepository;

  @Override
  public FileMetadata operate(UUID fileUuid) {
    FileMetadataEntity metadata = fileMetadataRepository.findById(fileUuid).orElseThrow();
    return new FileMetadata(metadata.getType(), metadata.getPath(), metadata.getName());
  }
}
