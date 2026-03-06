package io.dough.api.useCases.file.saveFile.application;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Set;

import io.dough.api.useCases.file.saveFile.application.model.*;
import io.dough.api.useCases.file.saveFile.domain.AcceptableFileType;
import io.dough.api.useCases.file.saveFile.domain.FileUploadType;
import io.dough.api.useCases.file.saveFile.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveFileService implements SaveFileUseCase {
  private final Set<SaveToFileStorage> fileUploadStrategies;
  private final SaveFileMetadata saveFileMetadata;
  private final Tika tika;

  private static final int MARK_READ_LIMIT = 64 * 1024;

  @Override
  public SaveFile operate(
      FileUploadType fileUploadType,
      InputStream inputStream,
      String originalFilename,
      long fileSize) {

    if (inputStream == null || originalFilename == null || originalFilename.isEmpty()) {
      throw new IllegalArgumentException("exception.file.invalid_input");
    }

    // ✦ 도메인 객체 생성 및 기본 확장자 검증
    UploadFile uploadFile = new UploadFile(fileUploadType, originalFilename, fileSize);

    try (InputStream bis = new BufferedInputStream(inputStream)) {
      bis.mark(MARK_READ_LIMIT);

      // ✦ 도메인 유효성 검증 (확장자 및 MIME 타입)
      AcceptableFileType acceptableFileType = uploadFile.validateExtension();
      String detectedMimeType = tika.detect(bis, originalFilename);
      uploadFile.validateMimeType(acceptableFileType, detectedMimeType);

      bis.reset();

      // ✦ 인프라 서비스 조율 (파일 저장소 업로드)
      getFileUploadStrategy(fileUploadType.getFileStorageType())
          .operate(bis, uploadFile.subPath(), uploadFile.createFilename());

      // ✦ 인프라 서비스 조율 (메타데이터 저장)
      SaveFileMetadataResult metadata =
          saveFileMetadata
              .operate(
                  new SaveFileMetadataCmd(
                      fileUploadType,
                      uploadFile.subPath(),
                      uploadFile.createFilename(),
                      originalFilename,
                      fileSize))
              .orElseThrow();

      return new SaveFile(
          metadata.id(),
          fileUploadType,
          uploadFile.subPath(),
          uploadFile.createFilename(),
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
