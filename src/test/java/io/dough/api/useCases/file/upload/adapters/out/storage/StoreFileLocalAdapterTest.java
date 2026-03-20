package io.dough.api.useCases.file.upload.adapters.out.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

class StoreFileLocalAdapterTest {

  private StoreFileLocalAdapter storeFileLocalAdapter;

  @TempDir Path tempDir;

  @BeforeEach
  void setUp() {
    storeFileLocalAdapter = new StoreFileLocalAdapter();
    // tempDir/public 경로를 생성하고 주입
    Path localPath = tempDir.resolve("public");
    ReflectionTestUtils.setField(storeFileLocalAdapter, "localPath", localPath.toString());
  }

  @Test
  @DisplayName("Scenario: 성공 - 파일을 로컬 스토리지에 저장한다")
  void save_file_success() throws IOException {
    // Given
    String content = "hello world";
    InputStream inputStream = new ByteArrayInputStream(content.getBytes());
    String subPath = "images/user";
    String filename = "test.png";

    // When
    storeFileLocalAdapter.operate(inputStream, subPath, filename);

    // Then
    Path savedFile = tempDir.resolve("public").resolve(subPath).resolve(filename);
    assertThat(Files.exists(savedFile)).isTrue();
    assertThat(Files.readString(savedFile)).isEqualTo(content);
    assertThat(Files.isReadable(savedFile)).isTrue();
  }

  @Test
  @DisplayName("Scenario: 성공 - 동일한 이름의 파일이 있으면 덮어쓴다")
  void save_file_overwrite() throws IOException {
    // Given
    String subPath = "images";
    String filename = "test.png";
    Path targetDir = tempDir.resolve("public").resolve(subPath);
    Files.createDirectories(targetDir);
    Path targetFile = targetDir.resolve(filename);
    Files.writeString(targetFile, "old content");

    String newContent = "new content";
    InputStream inputStream = new ByteArrayInputStream(newContent.getBytes());

    // When
    storeFileLocalAdapter.operate(inputStream, subPath, filename);

    // Then
    assertThat(Files.readString(targetFile)).isEqualTo(newContent);
  }

  @Test
  @DisplayName("Scenario: 실패 - 상위 디렉토리로 이동하는 경로(Path Traversal)는 차단된다")
  void save_file_fail_path_traversal() {
    // Given
    InputStream inputStream = new ByteArrayInputStream("hack".getBytes());
    String subPath = "images";
    String filename = "../../../etc/passwd";

    // When & Then
    assertThatThrownBy(() -> storeFileLocalAdapter.operate(inputStream, subPath, filename))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("exception.file.path_traversal");
  }
}
