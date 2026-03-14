package io.dough.api.useCases.file.upload.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import io.dough.api.useCases.file.resolveURL.application.ResolveURLUseCase;
import io.dough.api.useCases.file.upload.application.UploadFileUseCase;
import io.dough.api.useCases.file.upload.application.model.UploadedFile;
import io.dough.api.useCases.file.upload.application.model.UploadFileCmd;
import io.dough.api.useCases.shared.domain.file.FileUploadType;

import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(UploadFileWebAdapter.class)
class SaveFileWebAdapterTest extends WebMvcTestBase {

  @MockitoBean private UploadFileUseCase uploadFileUseCase;

  @MockitoBean private ResolveURLUseCase resolveUrlUseCase;

  @Test
  @DisplayName("Scenario: 성공 - Multipart 파일을 업로드한다")
  void upload_multipart_success() throws Exception {
    // Given
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "test.png", MediaType.IMAGE_PNG_VALUE, "test content".getBytes());
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;
    UUID fileId = UUID.randomUUID();
    UploadedFile uploadedFile =
        new UploadedFile(fileId, type, "/path/to/file", "saved.png", "test.png", 100L);
    String resourceUrl = "/uploads/path/to/file/saved.png";

    given(
            uploadFileUseCase.operate(
                any(UploadFileCmd.class)))
        .willReturn(uploadedFile);
    given(resolveUrlUseCase.operate(fileId)).willReturn(resourceUrl);

    // When & Then
    mockMvc
        .perform(multipart("/api/v1/upload/multipart").file(file).param("type", type.name()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.originName").value("test.png"))
        .andExpect(jsonPath("$.referURL").value(resourceUrl));

    verify(uploadFileUseCase)
        .operate(any(UploadFileCmd.class));
    verify(resolveUrlUseCase).operate(fileId);
  }

  @Test
  @DisplayName("Scenario: 성공 - Base64 데이터를 업로드한다")
  void upload_base64_success() throws Exception {
    // Given
    String base64Data =
        "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==";
    FileUploadType type = FileUploadType.UPLOAD_IMG_TO_LOCAL;

    // Enum이 @JsonFormat(Shape.OBJECT)로 설정되어 있어 objectMapper 사용 시 객체로 직렬화됨.
    // 서버의 @RequestBody 역직렬화 호환성을 위해 문자열로 직접 JSON 구성
    String requestBody =
        """
        {
          "type": "%s",
          "base64Data": "%s"
        }
        """
            .formatted(type.name(), base64Data);

    UUID fileId = UUID.randomUUID();
    UploadedFile uploadedFile =
        new UploadedFile(fileId, type, "/path/to/file", "saved.png", "image.png", 100L);
    String resourceUrl = "/uploads/path/to/file/saved.png";

    given(
            uploadFileUseCase.operate(
                any(UploadFileCmd.class)))
        .willReturn(uploadedFile);
    given(resolveUrlUseCase.operate(fileId)).willReturn(resourceUrl);

    // When & Then
    mockMvc
        .perform(
            post("/api/v1/upload/base64Data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.referURL").value(resourceUrl));

    verify(uploadFileUseCase)
        .operate(any(UploadFileCmd.class));
    verify(resolveUrlUseCase).operate(fileId);
  }
}
