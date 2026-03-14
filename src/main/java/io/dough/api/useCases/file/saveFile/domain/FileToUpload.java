package io.dough.api.useCases.file.saveFile.domain;

import io.dough.api.useCases.shared.domain.file.AcceptableFileType;
import io.dough.api.useCases.shared.domain.file.FileUploadType;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

/**
 * 업로드할 파일의 도메인 로직을 담당하는 객체
 */
public record FileToUpload(
    FileUploadType type,
    String originalFilename,
    long fileSize,
    String extension,
    String createFilename,
    String subPath) {
  public FileToUpload(FileUploadType type, String originalFilename, long fileSize) {
    this(
        type,
        originalFilename,
        fileSize,
        extractExtension(originalFilename),
        generateUUIDFilename(extractExtension(originalFilename)),
        calculateSubPath(type.getSubPath()));
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

  private static String calculateSubPath(String basePath) {
    LocalDate now = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
    return Path.of(basePath, now.format(formatter)).normalize().toString().replace("\\", "/");
  }

  /**
   * 파일 확장자가 허용된 타입인지 검증하고 해당하는 AcceptableFileType을 반환한다.
   */
  public AcceptableFileType validateExtension() {
    return type.getAcceptableFileTypes().stream()
        .filter(acceptableFileType -> acceptableFileType.getExtensions().contains(extension))
        .findAny()
        .orElseThrow(() -> new IllegalArgumentException("exception.file.invalid_extension"));
  }

  /**
   * 감지된 MIME 타입이 허용된 타입인지 검증한다.
   */
  public void validateMimeType(AcceptableFileType acceptableFileType, String detectedMimeType) {
    boolean isSupported = acceptableFileType.getMimeTypes().stream()
        .anyMatch(mimeType -> mimeType.equals(detectedMimeType));

    if (!isSupported) {
      throw new IllegalArgumentException("exception.file.invalid_mime_type");
    }
  }
}
