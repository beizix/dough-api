package io.dough.api.useCases.file.upload.adapters.out.persistence;

import io.dough.api.useCases.file.upload.application.port.out.FileMetadataRegistered;
import io.dough.api.useCases.file.upload.application.port.out.RegisterFileMetadata;
import io.dough.api.useCases.file.upload.application.port.out.RegisterFileMetadataCmd;
import io.dough.api.useCases.shared.adapters.out.persistence.entity.FileMetadataEntity;
import io.dough.api.useCases.shared.adapters.out.persistence.repository.FileMetadataRepository;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterFileMetadataPersistAdapter implements RegisterFileMetadata {
  private final FileMetadataRepository fileMetadataRepo;

  @Override
  public Optional<FileMetadataRegistered> operate(RegisterFileMetadataCmd cmd) {
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
        new FileMetadataRegistered(
            fileMetadata.getId(),
            fileMetadata.getType(),
            fileMetadata.getPath(),
            fileMetadata.getName(),
            fileMetadata.getOriginName(),
            fileMetadata.getFileLength()));
  }
}
