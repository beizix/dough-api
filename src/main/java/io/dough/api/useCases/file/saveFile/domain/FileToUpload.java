package io.dough.api.useCases.file.saveFile.domain;


import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * 업로드할 파일의 도메인 로직을 담당하는 객체
 */
public record FileToUpload(
    String originalFilename,
    long fileSize,
    String extension,
    String createFilename,
    String subPath) {
  public FileToUpload(String originalFilename, long fileSize, String basePath) {
    this(
        originalFilename,
        fileSize,
        extractExtension(originalFilename),
        generateUUIDFilename(extractExtension(originalFilename)),
        calculateSubPath(basePath));
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
   * 파일 확장자가 허용된 타입인지 검증한다.
   */
  public void validateExtension(Set<String> allowedExtensions) {
    if (!allowedExtensions.contains(extension)) {
      throw new IllegalArgumentException("exception.file.invalid_extension");
    }
  }

  /**
   * 감지된 MIME 타입이 허용된 타입인지 검증한다.
   */
  public void validateMimeType(Set<String> allowedMimeTypes, String detectedMimeType) {
    if (!allowedMimeTypes.contains(detectedMimeType)) {
      throw new IllegalArgumentException("exception.file.invalid_mime_type");
    }
  }
}
