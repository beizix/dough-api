package io.dough.api.useCases.file.resolveURL.adapters.out.storage;

import io.dough.api.useCases.file.resolveURL.application.port.out.ProvideFileURL;
import io.dough.api.useCases.shared.domain.file.FileStorageType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class ProvideS3FileURLAdapter implements ProvideFileURL {
  @Value("${spring.cloud.aws.s3.domain:#{null}}")
  private String cloudFrontDomain;

  @Value("${spring.cloud.aws.s3.folder:#{null}}")
  private String bucketFolder;

  @Override
  public FileStorageType getStorageType() {
    return FileStorageType.S3;
  }

  @Override
  public String operate(String path, String filename) {
    return UriComponentsBuilder.newInstance()
        .scheme("https")
        .host(cloudFrontDomain)
        .path("/" + bucketFolder)
        .path("/" + path)
        .path("/" + filename)
        .build()
        .toUriString();
  }
}
