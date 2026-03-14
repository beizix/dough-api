package io.dough.api.useCases.file.upload.application;

import io.dough.api.useCases.file.upload.application.model.UploadFileCmd;
import io.dough.api.useCases.file.upload.application.model.UploadedFile;

/** 주어진 입력 스트림을 비즈니스 규칙에 따라 저장소에 업로드하고, 파일 메타데이터를 생성합니다. */
public interface UploadFileUseCase {

  /**
   * 파일을 업로드하고 처리 결과를 반환합니다.
   *
   * @param cmd 파일 업로드 요청 커맨드 객체
   * @return 업로드 성공 시 생성된 파일 정보
   */
  UploadedFile operate(UploadFileCmd cmd);
}
