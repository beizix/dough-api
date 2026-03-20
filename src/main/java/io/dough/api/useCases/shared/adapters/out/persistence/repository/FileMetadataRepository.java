package io.dough.api.useCases.shared.adapters.out.persistence.repository;

import io.dough.api.useCases.shared.adapters.out.persistence.entity.FileMetadataEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository extends JpaRepository<FileMetadataEntity, UUID> {}
