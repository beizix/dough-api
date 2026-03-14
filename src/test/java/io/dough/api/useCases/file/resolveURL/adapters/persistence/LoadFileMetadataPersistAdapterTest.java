package io.dough.api.useCases.file.resolveURL.adapters.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.dough.api.support.DataJpaTestBase;
import io.dough.api.useCases.file.resolveURL.application.model.FileMetadataLoaded;
import io.dough.api.useCases.shared.adapters.persistence.entity.FileMetadataEntity;
import io.dough.api.useCases.shared.adapters.persistence.repository.FileMetadataRepository;
import io.dough.api.useCases.shared.domain.file.FileUploadType;

import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(LoadFileMetadataPersistAdapter.class)
class LoadFileMetadataPersistAdapterTest extends DataJpaTestBase {

  @Autowired private LoadFileMetadataPersistAdapter adapter;

  @Autowired private FileMetadataRepository repository;

  @Test
  @DisplayName("Scenario: 성공 - 파일 UUID로 리소스 정보를 조회한다")
  void get_file_resource_success() {
    // Given
    UUID fileId = UUID.randomUUID();
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;
    String path = "/images/202602";
    String name = "uuid-image.png";

    FileMetadataEntity entity =
        FileMetadataEntity.builder()
            .id(fileId)
            .type(type)
            .path(path)
            .name(name)
            .originName("original.png")
            .fileLength(1024L)
            .build();

    repository.save(entity);

    // When
    FileMetadataLoaded result = adapter.operate(fileId);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.fileUploadType()).isEqualTo(type);
    assertThat(result.path()).isEqualTo(path);
    assertThat(result.filename()).isEqualTo(name);
  }

  @Test
  @DisplayName("Scenario: 실패 - 존재하지 않는 파일 UUID 조회 시 예외 발생")
  void get_file_resource_fail_not_found() {
    // Given
    UUID invalidId = UUID.randomUUID();

    // When & Then
    assertThatThrownBy(() -> adapter.operate(invalidId)).isInstanceOf(NoSuchElementException.class);
  }
}
