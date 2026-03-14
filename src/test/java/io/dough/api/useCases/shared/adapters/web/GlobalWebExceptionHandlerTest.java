package io.dough.api.useCases.shared.adapters.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dough.api.support.WebMvcTestBase;
import java.util.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(controllers = {ExceptionTestController.class, GlobalWebExceptionHandler.class})
class GlobalWebExceptionHandlerTest extends WebMvcTestBase {

  @MockitoBean private MessageSource messageSource;

  @Test
  @DisplayName("Scenario: 성공 - NoSuchElementException 발생 시 400 응답과 메시지를 반환한다")
  void handleNoSuchElementException_success() throws Exception {
    // Given
    String errorCode = "error.notfound";
    String resolvedMessage = "찾을 수 없습니다.";
    given(messageSource.getMessage(eq(errorCode), any(), any(Locale.class)))
        .willReturn(resolvedMessage);

    // When & Then
    mockMvc
        .perform(get("/test/no-such-element").param("message", errorCode))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value(resolvedMessage))
        .andExpect(jsonPath("$.path").value("/test/no-such-element"));
  }

  @Test
  @DisplayName("Scenario: 성공 - IllegalArgumentException 발생 시 400 응답과 메시지를 반환한다")
  void handleIllegalArgumentException_success() throws Exception {
    // Given
    String errorCode = "error.invalid";
    String resolvedMessage = "잘못된 요청입니다.";
    given(messageSource.getMessage(eq(errorCode), any(), any(Locale.class)))
        .willReturn(resolvedMessage);

    // When & Then
    mockMvc
        .perform(get("/test/illegal-argument").param("message", errorCode))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"))
        .andExpect(jsonPath("$.message").value(resolvedMessage))
        .andExpect(jsonPath("$.path").value("/test/illegal-argument"));
  }

  @Test
  @DisplayName("Scenario: 성공 - 처리되지 않은 Exception 발생 시 500 응답을 반환한다")
  void handleGlobalException_success() throws Exception {
    // Given
    String message = "예상치 못한 서버 에러";

    // When & Then
    mockMvc
        .perform(get("/test/unhandled-exception").param("message", message))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.error").value("Internal Server Error"))
        .andExpect(jsonPath("$.message").value(message))
        .andExpect(jsonPath("$.path").value("/test/unhandled-exception"));
  }

  @Test
  @DisplayName("Scenario: 성공 - 메시지 소스에 없는 코드인 경우 원래 메시지를 반환한다")
  void resolveMessage_fallback_to_original() throws Exception {
    // Given
    String originalMessage = "original message";
    given(messageSource.getMessage(eq(originalMessage), any(), any(Locale.class)))
        .willThrow(new RuntimeException("Message not found"));

    // When & Then
    mockMvc
        .perform(get("/test/no-such-element").param("message", originalMessage))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(originalMessage));
  }

  @Test
  @DisplayName("Scenario: 성공 - 예외 메시지가 null인 경우 null을 반환한다")
  void resolveMessage_null() throws Exception {
    // When & Then
    mockMvc
        .perform(get("/test/no-such-element")) // message 파라미터 없음 -> null
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").isEmpty());
  }
}
