package io.dough.api.useCases.file.resolveURL.adapters.persistence;

import io.dough.api.useCases.file.resolveURL.application.LoadFileMetadata;
import io.dough.api.useCases.file.resolveURL.application.model.FileMetadataLoaded;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.FileMetadataEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.FileMetadataRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoadFileMetadataPersistAdapter implements LoadFileMetadata {
  private final FileMetadataRepository fileMetadataRepository;

  @Override
  public FileMetadataLoaded operate(UUID fileUuid) {
    FileMetadataEntity metadata = fileMetadataRepository.findById(fileUuid).orElseThrow();
    return new FileMetadataLoaded(metadata.getType(), metadata.getPath(), metadata.getName());
  }
}
