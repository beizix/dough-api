package io.dough.api.useCases.file.upload.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.lenient;

import io.dough.api.useCases.file.upload.application.port.in.UploadFileCmd;
import io.dough.api.useCases.file.upload.application.port.in.UploadedFile;
import io.dough.api.useCases.file.upload.application.port.out.FileMetadataRegistered;
import io.dough.api.useCases.file.upload.application.port.out.RegisterFileMetadata;
import io.dough.api.useCases.file.upload.application.port.out.RegisterFileMetadataCmd;
import io.dough.api.useCases.file.upload.application.port.out.StoreFile;
import io.dough.api.useCases.shared.domain.file.FileStorageType;
import io.dough.api.useCases.shared.domain.file.FileUploadType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UploadFileServiceTest {

  @Mock private RegisterFileMetadata registerFileMetadata;

  @Mock private Tika tika;

  @Mock private StoreFile localStorageStrategy;

  private UploadFileService uploadFileService;

  @BeforeEach
  void setUp() {
    // 기본적으로 로컬 스토리지 전략을 지원하도록 설정
    // 일부 실패 테스트에서는 전략을 조회하지 않으므로 lenient() 사용
    lenient().when(localStorageStrategy.getStorageType()).thenReturn(FileStorageType.LOCAL);

    uploadFileService = new UploadFileService(Set.of(localStorageStrategy), registerFileMetadata, tika);
  }

  @Test
  @DisplayName("Scenario: 성공 - 유효한 이미지 파일을 업로드한다")
  void upload_success() throws IOException {
    // Given
    InputStream inputStream = new ByteArrayInputStream("test image content".getBytes());
    String originalFilename = "test.png";
    long fileSize = 1024L;
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;
    UUID expectedId = UUID.randomUUID();

    given(tika.detect(any(InputStream.class), eq(originalFilename))).willReturn("image/png");
    given(registerFileMetadata.operate(any(RegisterFileMetadataCmd.class)))
        .willReturn(
            Optional.of(
                new FileMetadataRegistered(
                    expectedId, type, "/path", "uuid.png", originalFilename, fileSize)));

    // When
    UploadFileCmd cmd = new UploadFileCmd(type, inputStream, originalFilename, fileSize);
    UploadedFile result = uploadFileService.operate(cmd);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(expectedId);
    assertThat(result.originName()).isEqualTo(originalFilename);

    then(localStorageStrategy).should().operate(any(InputStream.class), anyString(), anyString());
    then(registerFileMetadata).should().operate(any(RegisterFileMetadataCmd.class));
  }

  @Test
  @DisplayName("Scenario: 실패 - 파일 확장자가 없는 경우 예외 발생")
  void upload_fail_no_extension() {
    // Given
    InputStream inputStream = new ByteArrayInputStream("content".getBytes());
    String originalFilename = "testfile"; // No extension

    // When & Then
    assertThatThrownBy(
            () -> {
              UploadFileCmd cmd =
                  new UploadFileCmd(
                      FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, originalFilename, 100L);
              uploadFileService.operate(cmd);
            })
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("exception.file.no_extension");
  }

  @Test
  @DisplayName("Scenario: 실패 - 허용되지 않는 파일 확장자인 경우 예외 발생")
  void upload_fail_invalid_extension() {
    // Given
    InputStream inputStream = new ByteArrayInputStream("content".getBytes());
    String originalFilename = "test.exe"; // Not allowed for USER_IMAGE

    // When & Then
    assertThatThrownBy(
            () -> {
              UploadFileCmd cmd =
                  new UploadFileCmd(
                      FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, originalFilename, 100L);
              uploadFileService.operate(cmd);
            })
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("exception.file.invalid_extension");
  }

  @Test
  @DisplayName("Scenario: 실패 - 확장자와 일치하지 않는 MIME Type 인 경우 예외 발생")
  void upload_fail_mismatch_mimetype() throws IOException {
    // Given
    InputStream inputStream = new ByteArrayInputStream("fake image".getBytes());
    String originalFilename = "test.png";

    // Tika가 실행 파일로 감지
    given(tika.detect(any(InputStream.class), eq(originalFilename)))
        .willReturn("application/x-dosexec");

    // When & Then
    assertThatThrownBy(
            () -> {
              UploadFileCmd cmd =
                  new UploadFileCmd(
                      FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, originalFilename, 100L);
              uploadFileService.operate(cmd);
            })
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("exception.file.invalid_mime_type");
  }

  @Test
  @DisplayName("Scenario: 실패 - 지원하지 않는 스토리지 타입인 경우 예외 발생")
  void upload_fail_no_strategy() throws IOException {
    // Given: 지원하는 전략이 비어있는 서비스 생성
    UploadFileService noStrategyService =
        new UploadFileService(Set.of(), registerFileMetadata, tika);

    InputStream inputStream = new ByteArrayInputStream("content".getBytes());
    String originalFilename = "test.png";

    given(tika.detect(any(InputStream.class), eq(originalFilename))).willReturn("image/png");

    // When & Then
    assertThatThrownBy(
            () -> {
              UploadFileCmd cmd =
                  new UploadFileCmd(
                      FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, originalFilename, 100L);
              noStrategyService.operate(cmd);
            })
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("exception.file.no_store_implementation");
  }

  @Test
  @DisplayName("Scenario: 실패 - 입력값이 유효하지 않은 경우 예외 발생")
  void upload_fail_invalid_input() {
    // Given
    InputStream inputStream = new ByteArrayInputStream("content".getBytes());

    // When & Then
    assertThatThrownBy(
            () -> {
              UploadFileCmd cmd =
                  new UploadFileCmd(FileUploadType.UPLOAD_IMG_TO_LOCAL, null, "test.png", 100L);
              uploadFileService.operate(cmd);
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.file.invalid_input");

    assertThatThrownBy(
            () -> {
              UploadFileCmd cmd =
                  new UploadFileCmd(FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, null, 100L);
              uploadFileService.operate(cmd);
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.file.invalid_input");

    assertThatThrownBy(
            () -> {
              UploadFileCmd cmd =
                  new UploadFileCmd(FileUploadType.UPLOAD_IMG_TO_LOCAL, inputStream, "", 100L);
              uploadFileService.operate(cmd);
            })
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("exception.file.invalid_input");
  }
}
