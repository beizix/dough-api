package io.dough.api.useCases.file.upload.application.service;

import io.dough.api.useCases.file.upload.application.port.in.UploadFileCmd;
import io.dough.api.useCases.file.upload.application.port.in.UploadFileUseCase;
import io.dough.api.useCases.file.upload.application.port.in.UploadedFile;
import io.dough.api.useCases.file.upload.application.port.out.FileMetadataRegistered;
import io.dough.api.useCases.file.upload.application.port.out.RegisterFileMetadata;
import io.dough.api.useCases.file.upload.application.port.out.RegisterFileMetadataCmd;
import io.dough.api.useCases.file.upload.application.port.out.StoreFile;
import io.dough.api.useCases.file.upload.domain.model.UploadableFile;
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
  private final Set<StoreFile> storeFileStrategies;
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
      getStoreFileStrategy(fileUploadType.getFileStorageType())
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

  private StoreFile getStoreFileStrategy(FileStorageType fileStorageType) {
    return storeFileStrategies.stream()
        .filter(storeFile -> storeFile.getStorageType().equals(fileStorageType))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException("exception.file.no_strategy"));
  }
}
