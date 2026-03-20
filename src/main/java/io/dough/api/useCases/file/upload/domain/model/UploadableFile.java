package io.dough.api.useCases.file.upload.domain.model;

import io.dough.api.useCases.shared.domain.file.FileUploadType;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

/** 업로드할 파일의 도메인 로직을 담당하는 객체 */
public record UploadableFile(
    FileUploadType fileUploadType,
    String originalFilename,
    long fileSize,
    String extension,
    String createFilename) {

  public UploadableFile(FileUploadType fileUploadType, String originalFilename, long fileSize) {
    this(
        fileUploadType,
        originalFilename,
        fileSize,
        extractExtension(originalFilename),
        generateUUIDFilename(extractExtension(originalFilename)));
  }

  /** 실제 물리 파일이 저장될 하위 경로를 계산하여 반환합니다. */
  public String subPath() {
    LocalDate now = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
    return Path.of(fileUploadType.getSubPath(), now.format(formatter))
        .normalize()
        .toString()
        .replace("\\", "/");
  }

  private static String extractExtension(String filename) {
    return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".") + 1).toLowerCase())
        .orElseThrow(() -> new IllegalArgumentException("exception.file.no_extension"));
  }

  private static String generateUUIDFilename(String extension) {
    return UUID.randomUUID() + "." + extension;
  }

  /** 파일 확장자가 허용된 타입인지 검증한다. */
  public void validateExtension() {
    boolean isAllowed =
        fileUploadType.getAcceptableFileTypes().stream()
            .anyMatch(type -> type.getExtensions().contains(extension));

    if (!isAllowed) {
      throw new IllegalArgumentException("exception.file.invalid_extension");
    }
  }

  /** 감지된 MIME 타입이 허용된 타입인지 검증한다. */
  public void validateMimeType(String detectedMimeType) {
    boolean isAllowed =
        fileUploadType.getAcceptableFileTypes().stream()
            .anyMatch(type -> type.getMimeTypes().contains(detectedMimeType));

    if (!isAllowed) {
      throw new IllegalArgumentException("exception.file.invalid_mime_type");
    }
  }
}


