package io.dough.api.useCases.file.upload.application;

import io.dough.api.useCases.file.upload.application.model.*;
import io.dough.api.useCases.file.upload.domain.*;
import io.dough.api.useCases.shared.domain.file.FileStorageType;
import io.dough.api.useCases.shared.domain.file.FileUploadType;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadFileService implements UploadFileUseCase {
  private final Set<SaveToFileStorage> fileUploadStrategies;
  private final RegisterFileMetadata registerFileMetadata;
  private final Tika tika;

  private static final int MARK_READ_LIMIT = 64 * 1024;

  @Override
  public UploadedFile operate(UploadFileCmd cmd) {

    FileUploadType fileUploadType = cmd.fileUploadType();
    String originalFilename = cmd.originalFilename();
    long fileSize = cmd.fileSize();

    // ✦ 도메인 객체 생성 및 기본 확장자 검증
    UploadableFile uploadableFile =
        new UploadableFile(originalFilename, fileSize, fileUploadType.getSubPath());

    uploadableFile.validateExtension(cmd.getAllowedExtensions());

    try (InputStream bis = new BufferedInputStream(cmd.inputStream())) {
      bis.mark(MARK_READ_LIMIT);

      // ✦ 도메인 유효성 검증 (MIME 타입)
      String detectedMimeType = tika.detect(bis, originalFilename);
      uploadableFile.validateMimeType(cmd.getAllowedMimeTypes(), detectedMimeType);

      bis.reset();

      // ✦ 인프라 서비스 조율 (파일 저장소 업로드)
      getFileUploadStrategy(fileUploadType.getFileStorageType())
          .operate(bis, uploadableFile.subPath(), uploadableFile.createFilename());

      // ✦ 인프라 서비스 조율 (메타데이터 저장)
      FileMetadataRegistered metadata =
          registerFileMetadata
              .operate(
                  new RegisterFileMetadataCmd(
                      fileUploadType,
                      uploadableFile.subPath(),
                      uploadableFile.createFilename(),
                      originalFilename,
                      fileSize))
              .orElseThrow();

      return new UploadedFile(
          metadata.id(),
          fileUploadType,
          uploadableFile.subPath(),
          uploadableFile.createFilename(),
          originalFilename,
          fileSize);

    } catch (IOException e) {
      throw new RuntimeException("exception.file.unexpected_error", e);
    }
  }

  private SaveToFileStorage getFileUploadStrategy(FileStorageType fileStorageType) {
    return fileUploadStrategies.stream()
        .filter(saveToFileStorage -> saveToFileStorage.getStorageType().equals(fileStorageType))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("exception.file.no_strategy"));
  }
}
