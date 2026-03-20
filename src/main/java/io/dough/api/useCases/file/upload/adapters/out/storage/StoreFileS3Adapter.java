package io.dough.api.useCases.file.upload.adapters.storage;

import io.awspring.cloud.s3.S3Template;
import io.dough.api.useCases.file.upload.application.port.out.StoreFile;
import io.dough.api.useCases.shared.domain.file.FileStorageType;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.s3.enabled", havingValue = "true")
public class StoreFileS3Adapter implements StoreFile {
  private final S3Template s3Template;

  @Value("${spring.cloud.aws.s3.bucket:#{null}}")
  private String bucketName;

  @Value("${spring.cloud.aws.s3.folder:#{null}}")
  private String bucketFolder;

  @Override
  public FileStorageType getStorageType() {
    return FileStorageType.S3;
  }

  @Override
  public void operate(InputStream inputStream, String createSubPath, String createFilename)
      throws IOException {

    String path =
        UriComponentsBuilder.fromPath(bucketFolder)
            .path("/" + createSubPath)
            .path("/" + createFilename)
            .build()
            .toUriString();

    s3Template.upload(bucketName, path, inputStream);
  }
}
