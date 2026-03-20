package io.dough.api.useCases.file.upload.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.dough.api.useCases.shared.domain.file.FileUploadType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UploadableFileTest {

  @Test
  @DisplayName("Scenario: 성공 - 유효한 파일명으로 UploadableFile 생성 시 확장자와 UUID 파일명이 생성된다")
  void create_uploadable_file_success() {
    // Given
    String originalFilename = "test-image.png";
    long fileSize = 1024L;
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;

    // When
    UploadableFile uploadableFile = new UploadableFile(type, originalFilename, fileSize);

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
    FileUploadType type = FileUploadType.MY_PROFILE_IMG; // subPath: /user/profile/img
    String expectedYearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));

    // When
    UploadableFile uploadableFile = new UploadableFile(type, originalFilename, 100L);

    // Then
    assertThat(uploadableFile.subPath()).contains(expectedYearMonth);
    assertThat(uploadableFile.subPath()).startsWith("/user/profile/img");
  }

  @Test
  @DisplayName("Scenario: 실패 - 확장자가 없는 파일명은 생성 시 예외가 발생한다")
  void create_file_without_extension_fail() {
    // Given
    String filenameWithoutExtension = "no-extension-file";
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;

    // When & Then
    assertThatThrownBy(() -> new UploadableFile(type, filenameWithoutExtension, 100L))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.file.no_extension");
  }

  @Test
  @DisplayName("Scenario: 성공 - 허용된 확장자인 경우 검증을 통과한다")
  void validate_extension_success() {
    // Given
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;
    UploadableFile uploadableFile = new UploadableFile(type, "image.png", 100L);

    // When & Then (No exception)
    uploadableFile.validateExtension();
  }

  @Test
  @DisplayName("Scenario: 실패 - 허용되지 않은 확장자인 경우 예외가 발생한다")
  void validate_extension_fail() {
    // Given
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;
    UploadableFile uploadableFile = new UploadableFile(type, "virus.exe", 100L);

    // When & Then
    assertThatThrownBy(uploadableFile::validateExtension)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.file.invalid_extension");
  }

  @Test
  @DisplayName("Scenario: 성공 - 허용된 MIME 타입인 경우 검증을 통과한다")
  void validate_mime_type_success() {
    // Given
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;
    UploadableFile uploadableFile = new UploadableFile(type, "image.png", 100L);
    String detectedMimeType = "image/png";

    // When & Then (No exception)
    uploadableFile.validateMimeType(detectedMimeType);
  }

  @Test
  @DisplayName("Scenario: 실패 - 허용되지 않은 MIME 타입인 경우 예외가 발생한다")
  void validate_mime_type_fail() {
    // Given
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;
    UploadableFile uploadableFile = new UploadableFile(type, "image.png", 100L);
    String detectedMimeType = "application/pdf";

    // When & Then
    assertThatThrownBy(() -> uploadableFile.validateMimeType(detectedMimeType))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.file.invalid_mime_type");
  }
}


