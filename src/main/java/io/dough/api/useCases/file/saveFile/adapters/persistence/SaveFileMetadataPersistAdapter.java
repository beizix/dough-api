package io.dough.api.useCases.file.saveFile.adapters.persistence;

import io.dough.api.common.adapters.persistence.entity.FileMetadataEntity;
import io.dough.api.common.adapters.persistence.repository.FileMetadataRepository;
import io.dough.api.useCases.file.saveFile.application.SaveFileMetadata;
import io.dough.api.useCases.file.saveFile.application.domain.model.SaveFileMetadataCmd;
import io.dough.api.useCases.file.saveFile.application.domain.model.SaveFileMetadataResult;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveFileMetadataPersistAdapter implements SaveFileMetadata {
  private final FileMetadataRepository fileMetadataRepo;

  @Override
  public Optional<SaveFileMetadataResult> operate(SaveFileMetadataCmd cmd) {
    FileMetadataEntity fileMetadata =
        fileMetadataRepo.save(
            new FileMetadataEntity(
                UUID.randomUUID(),
                cmd.type(),
                cmd.path(),
                cmd.name(),
                cmd.originName(),
                cmd.fileLength()));

    return Optional.of(
        new SaveFileMetadataResult(
            fileMetadata.getId(),
            fileMetadata.getType(),
            fileMetadata.getPath(),
            fileMetadata.getName(),
            fileMetadata.getOriginName(),
            fileMetadata.getFileLength()));
  }
}
