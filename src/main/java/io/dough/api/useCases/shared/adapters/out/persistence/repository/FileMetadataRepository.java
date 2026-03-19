package io.dough.api.useCases.shared.adapters.out.persistence.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import io.dough.api.useCases.shared.adapters.out.persistence.entity.FileMetadataEntity;

public interface FileMetadataRepository extends JpaRepository<FileMetadataEntity, UUID> {}
