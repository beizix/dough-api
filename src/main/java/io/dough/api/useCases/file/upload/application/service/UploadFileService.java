package io.dough.api.useCases.file.upload.application.service;

import io.dough.api.useCases.file.upload.application.port.in.UploadFileCmd;
import io.dough.api.useCases.file.upload.application.port.in.UploadFileUseCase;
import io.dough.api.useCases.file.upload.application.port.in.UploadedFile;
import io.dough.api.useCases.file.upload.application.port.out.FileMetadataRegistered;
import io.dough.api.useCases.file.upload.application.port.out.RegisterFileMetadata;
import io.dough.api.useCases.file.upload.application.port.out.RegisterFileMetadataCmd;
import io.dough.api.useCases.file.upload.domain.model.UploadableFile;
import io.dough.api.useCases.file.upload.domain.service.StoreFile;
import io.dough.api.useCases.shared.domain.file.FileUploadType;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadFileService implements UploadFileUseCase {
  private final Set<StoreFile> storeFiles;
  private final RegisterFileMetadata registerFileMetadata;
  private final Tika tika;

  private static final int MARK_READ_LIMIT = 64 * 1024;

  @Override
  public UploadedFile operate(UploadFileCmd cmd) {

    FileUploadType fileUploadType = cmd.fileUploadType();
    String originalFilename = cmd.originalFilename();
    long fileSize = cmd.fileSize();

    // ✦ 도메인 객체 생성 및 기본 확장자 검증
    UploadableFile uploadableFile = new UploadableFile(fileUploadType, originalFilename, fileSize);

    uploadableFile.validateExtension();

    try (InputStream bis = new BufferedInputStream(cmd.inputStream())) {
      bis.mark(MARK_READ_LIMIT);

      // ✦ 도메인 유효성 검증 (MIME 타입)
      String detectedMimeType = tika.detect(bis, originalFilename);
      uploadableFile.validateMimeType(detectedMimeType);

      bis.reset();

      // ✦ 적합한 저장소 전략을 선택하여 물리 파일 저장 (Local, S3 등)
      uploadableFile
          .resolveStore(storeFiles)
          .operate(bis, uploadableFile.subPath(), uploadableFile.createFilename());

      // ✦ 파일 메타데이터 영속화 및 DB 등록
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
}
