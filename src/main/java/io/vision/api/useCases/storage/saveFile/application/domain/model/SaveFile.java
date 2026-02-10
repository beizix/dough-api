package io.vision.api.useCases.storage.saveFile.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class SaveFile {
  private UUID id;
  private FileUploadType type;
  private String path;
  private String name;
  private String originName;
  private Long fileLength;
}
