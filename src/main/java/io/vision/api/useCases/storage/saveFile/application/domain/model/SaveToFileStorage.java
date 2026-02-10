package io.vision.api.useCases.storage.saveFile.application.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class SaveToFileStorage {
  private FileUploadType type;
  private String path;
  private String name;
  private String originName;
  private Long fileLength;
}
