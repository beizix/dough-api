package io.dough.api.useCases.file.upload.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UploadableFileTest {

  @Test
  @DisplayName("Scenario: 성공 - 유효한 파일명으로 UploadableFile 생성 시 확장자와 UUID 파일명이 생성된다")
  void create_uploadable_file_success() {
    // Given
    String originalFilename = "test-image.png";
    long fileSize = 1024L;
    String basePath = "uploads";

    // When
    UploadableFile uploadableFile = new UploadableFile(originalFilename, fileSize, basePath);

    // Then
    assertThat(uploadableFile.originalFilename()).isEqualTo(originalFilename);
    assertThat(uploadableFile.fileSize()).isEqualTo(fileSize);
    assertThat(uploadableFile.extension()).isEqualTo("png");
    assertThat(uploadableFile.createFilename()).endsWith(".png");
    assertThat(uploadableFile.createFilename()).isNotEqualTo(originalFilename);
  }

  @Test
  @DisplayName("Scenario: 성공 - 서브 경로는 현재 날짜 기반의 yyyyMM 형식을 포함한다")
  void calculate_sub_path_success() {
    // Given
    String originalFilename = "test.jpg";
    String basePath = "profiles";
    String expectedYearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

    // When
    UploadableFile uploadableFile = new UploadableFile(originalFilename, 100L, basePath);

    // Then
    assertThat(uploadableFile.subPath()).contains(expectedYearMonth);
    assertThat(uploadableFile.subPath()).startsWith("profiles/");
  }

  @Test
  @DisplayName("Scenario: 실패 - 확장자가 없는 파일명은 생성 시 예외가 발생한다")
  void create_file_without_extension_fail() {
    // Given
    String filenameWithoutExtension = "no-extension-file";

    // When & Then
    assertThatThrownBy(() -> new UploadableFile(filenameWithoutExtension, 100L, "temp"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.file.no_extension");
  }

  @Test
  @DisplayName("Scenario: 성공 - 허용된 확장자 세트에 포함된 경우 검증을 통과한다")
  void validate_extension_success() {
    // Given
    UploadableFile uploadableFile = new UploadableFile("image.png", 100L, "base");
    Set<String> allowedExtensions = Set.of("png", "jpg", "jpeg");

    // When & Then (No exception)
    uploadableFile.validateExtension(allowedExtensions);
  }

  @Test
  @DisplayName("Scenario: 실패 - 허용되지 않은 확장자인 경우 예외가 발생한다")
  void validate_extension_fail() {
    // Given
    UploadableFile uploadableFile = new UploadableFile("virus.exe", 100L, "base");
    Set<String> allowedExtensions = Set.of("png", "jpg", "jpeg");

    // When & Then
    assertThatThrownBy(() -> uploadableFile.validateExtension(allowedExtensions))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.file.invalid_extension");
  }

  @Test
  @DisplayName("Scenario: 성공 - 허용된 MIME 타입인 경우 검증을 통과한다")
  void validate_mime_type_success() {
    // Given
    UploadableFile uploadableFile = new UploadableFile("image.png", 100L, "base");
    Set<String> allowedMimeTypes = Set.of("image/png", "image/jpeg");
    String detectedMimeType = "image/png";

    // When & Then (No exception)
    uploadableFile.validateMimeType(allowedMimeTypes, detectedMimeType);
  }

  @Test
  @DisplayName("Scenario: 실패 - 허용되지 않은 MIME 타입인 경우 예외가 발생한다")
  void validate_mime_type_fail() {
    // Given
    UploadableFile uploadableFile = new UploadableFile("image.png", 100L, "base");
    Set<String> allowedMimeTypes = Set.of("image/png", "image/jpeg");
    String detectedMimeType = "application/pdf";

    // When & Then
    assertThatThrownBy(() -> uploadableFile.validateMimeType(allowedMimeTypes, detectedMimeType))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.file.invalid_mime_type");
  }
}
