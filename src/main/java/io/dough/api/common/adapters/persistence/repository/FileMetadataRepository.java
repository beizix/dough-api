package io.dough.api.common.adapters.persistence.repository;

import io.dough.api.common.adapters.persistence.entity.FileMetadataEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository extends JpaRepository<FileMetadataEntity, UUID> {}
