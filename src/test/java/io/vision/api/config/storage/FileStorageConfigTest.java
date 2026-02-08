package io.vision.api.config.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

class FileStorageConfigTest {

  private FileStorageConfig fileStorageConfig;

  @TempDir
  Path tempDir;

  @BeforeEach
  void setUp() {
    fileStorageConfig = new FileStorageConfig();
  }

  @Test
  @DisplayName("Scenario: 성공 - 설정된 경로에 디렉토리가 존재하지 않으면 생성한다")
  void initialize_success_create_directories() throws IOException {
    // Given
    Path publicPath = tempDir.resolve("public");
    Path tmpPath = tempDir.resolve("tmp");

    ReflectionTestUtils.setField(fileStorageConfig, "publicPath", publicPath.toString());
    ReflectionTestUtils.setField(fileStorageConfig, "tmpPath", tmpPath.toString());

    // When
    fileStorageConfig.initialize();

    // Then
    assertThat(Files.exists(publicPath)).isTrue();
    assertThat(Files.exists(tmpPath)).isTrue();
  }

  @Test
  @DisplayName("Scenario: 성공 - 경로가 null이면 디렉토리를 생성하지 않는다")
  void initialize_do_nothing_when_path_is_null() throws IOException {
    // Given
    ReflectionTestUtils.setField(fileStorageConfig, "publicPath", null);
    ReflectionTestUtils.setField(fileStorageConfig, "tmpPath", null);

    // When
    fileStorageConfig.initialize();

    // Then
    assertThat(tempDir.toFile().list()).isEmpty();
  }

  @Test
  @DisplayName("Scenario: 성공 - 경로가 빈 문자열이면 디렉토리를 생성하지 않는다")
  void initialize_do_nothing_when_path_is_blank() throws IOException {
    // Given
    ReflectionTestUtils.setField(fileStorageConfig, "publicPath", " ");
    ReflectionTestUtils.setField(fileStorageConfig, "tmpPath", "");

    // When
    fileStorageConfig.initialize();

    // Then
    assertThat(tempDir.toFile().list()).isEmpty();
  }
}
