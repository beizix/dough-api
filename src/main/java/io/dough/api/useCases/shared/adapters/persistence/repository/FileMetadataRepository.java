package io.dough.api.useCases.shared.adapters.persistence.repository;

import io.dough.api.useCases.shared.adapters.persistence.entity.FileMetadataEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository extends JpaRepository<FileMetadataEntity, UUID> {}
