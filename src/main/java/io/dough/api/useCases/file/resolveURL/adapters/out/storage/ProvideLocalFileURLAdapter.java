package io.dough.api.useCases.file.resolveURL.adapters.out.storage;

import io.dough.api.useCases.file.resolveURL.application.port.out.ProvideFileURL;
import io.dough.api.useCases.shared.domain.file.FileStorageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class ProvideLocalFileURLAdapter implements ProvideFileURL {

  private static final String URL_PREFIX = "/uploads/";

  @Override
  public FileStorageType getStorageType() {
    return FileStorageType.LOCAL;
  }

  @Override
  public String operate(String path, String filename) {
    if (path == null || filename == null) {
      return "";
    }

    return UriComponentsBuilder.fromPath(URL_PREFIX)
        .path("/" + path)
        .path("/" + filename)
        .build()
        .toUriString();
  }
}
