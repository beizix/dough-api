# Internationalization (i18n) & Messaging Guide

이 문서는 Dough API 프로젝트의 다국어 메시징 처리 방식과 운영 환경에서의 동작 원리를 설명합니다.

## 1. 개요 (Overview)

우리 시스템은 사용자에게 제공되는 메시지(특히 예외 메시지)의 유지보수성을 높이고 글로벌 서비스를 준비하기 위해 **Spring Boot의 정적 메시징 처리(i18n)** 방식을 채택하고 있습니다. 소스코드에 메시지를 직접 하드코딩하지 않고 관리 포인트가 일원화된 프로퍼티 파일을 사용합니다.

- **설정 클래스**: `MessageConfig.java`
*   **유틸리티 클래스**: `MessageUtils.java`
*   **메시지 리소스 경로**: `src/main/resources/messages*.properties`

---

## 2. 운영 환경에서의 동작 원리 (Runtime Behavior)

서버가 구동될 때 `MessageConfig`와 `LocaleResolver` 설정에 따라 다음과 같이 동작합니다.

### 2.1 기본 언어 (Default Locale)
*   **파일**: `messages.properties`
*   별도의 언어 요청이 없거나 지원하지 않는 언어일 경우 **영문(English)** 메시지가 기본으로 제공됩니다.

### 2.2 언어 자동 감지 (Locale Resolution)
*   프로젝트는 `AcceptHeaderLocaleResolver`를 사용합니다.
*   클라이언트가 HTTP 요청 헤더에 `Accept-Language: ko`를 포함하여 보내면, 서버는 자동으로 이를 감지하여 **한글(`messages_ko.properties`)** 메시지를 반환합니다.

---

## 3. 개발 가이드라인 (Developer Guidelines)

### 3.1 메시지 정의하기
새로운 메시지가 필요한 경우 각 언어별 프로퍼티 파일에 동일한 키값으로 메시지를 추가합니다.

**`messages.properties`**
```properties
exception.user.not_found=User not found
exception.auth.invalid_password=Invalid password
```

**`messages_ko.properties`**
```properties
exception.user.not_found=사용자를 찾을 수 없습니다.
exception.auth.invalid_password=잘못된 비밀번호입니다.
```

### 3.2 메시지 사용하기

#### 서비스 레이어 (Bean 주입)
`MessageUtils`를 주입받아 메시지를 가져옵니다.
```java
private final MessageUtils messageUtils;

public void someMethod() {
    throw new IllegalArgumentException(messageUtils.getMessage("exception.user.not_found"));
}
```

#### 일반 클래스 (정적 메서드)
Spring Bean이 아닌 곳에서도 `MessageUtils.get()`을 통해 메시지를 가져올 수 있습니다.
```java
public class Base64MultipartFile {
    public void someMethod() {
        throw new IllegalArgumentException(MessageUtils.get("exception.file.invalid_base64"));
    }
}
```

### 3.3 인자(Argument) 전달하기
메시지에 변수가 포함되는 경우 `{0}`, `{1}` 등을 사용하여 인자를 전달할 수 있습니다.

**Property:** `exception.file.no_extension='{0}' - File extension does not exist.`

**Usage:**
```java
messageUtils.getMessage("exception.file.no_extension", new Object[] { originalFilename });
```

---

## 4. 테스트 전략 (Testing Strategy)

단위 테스트 시에는 `MessageUtils`를 Mock으로 만들어 특정 메시지를 반환하도록 설정합니다. 이는 실제 서버의 다국어 설정과는 독립적으로, "특정 로직에서 적절한 메시지 키를 호출하는가"를 검증하기 위함입니다.

```java
@Mock private MessageUtils messageUtils;

@Test
void test() {
    String expectedMsg = "가짜 에러 메시지";
    when(messageUtils.getMessage(eq("some.key"), any())).thenReturn(expectedMsg);
    
    assertThatThrownBy(() -> service.operate())
        .hasMessageContaining(expectedMsg);
}
```
