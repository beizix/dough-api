## Gemini Added Memories
- Always respond in Korean.
- Always write commit messages in Korean.

# 커밋 규칙

사용자가 명시적으로 요청하지 않는 이상 git 관련 명령은 수행하지 않습니다.

### 사용자가 `커밋` 혹은 `commit` 를 입력하면 커밋 작업을 수행합니다.

-   git add . 를 수행해서 전체 수정 내역에 대한 커밋을 수행합니다.
-   커밋 메시지 앞에 항상 ✦ 기호를 붙여주세요.
-   커밋 메시지 작성 시 'feat', 'refactor', 'chore', 'test' 등과 같은 핵심 문구를 포함해야 합니다.
-   커밋 완료 후 git status 는 실행할 필요 없습니다.

### 사용자가 `pr desc` 을 입력하면 Pull Request 설명에 기입할 내용을 요약해서 보여줍니다.
-   PR 은 사용자가 수동을 생성하니 동기화 및 푸시는 수행하지 않습니다.
-   단순히 커밋 메세지를 나열하는게 아니라 최근 추가된 커밋의 내용을 몇 문장으로 요약해서 보여줍니다.

---

# 🚀 TDD 및 개발 프로세스 (Outside-In)

이 프로젝트는 **Outside-In TDD (London School)** 방식을 지향합니다. API의 진입점부터 시작하여 내부 도메인으로 들어가는 순서로 개발합니다.

**권장 개발 순서:**
1.  **Web Adapter Test (Controller)**: 클라이언트의 요구사항(API 스펙)을 정의하는 실패하는 테스트 작성
2.  **API Interface & DTO**: 컴파일 에러를 해결하기 위한 최소한의 인터페이스 정의
3.  **Web Adapter Implement**: Service를 Mocking하여 컨트롤러 구현 및 테스트 통과
4.  **Application Layer Test (Service)**: 비즈니스 로직에 대한 실패하는 테스트 작성
5.  **Application Implement**: Port(Repository)를 Mocking하여 서비스 로직 구현
6.  **Persistence Adapter Test**: 실제 DB와의 연동을 검증하는 테스트 작성
7.  **Persistence Implement**: 쿼리 및 매핑 로직 구현

---

# 파일 생성 규칙

- 탭 간격은 공백 2칸으로 설정합니다.
- TDD 사이클에 맞춰 **필요한 시점에 필요한 파일만** 생성하는 것을 원칙으로 합니다.
- 단, 헥사고날 아키텍처의 패키지 구조는 유지합니다.

# Java 코딩 규칙

- 모든 객체 생성은 `new` 키워드를 사용한 **생성자 호출 방식**으로 통일합니다. (Lombok `@Builder` 지양)
- **Record Class**: DTO, Command, 도메인 불변 객체는 `record` 타입을 적극 활용합니다.

---

# 헥사고날 아키텍처 파일 구조

파일은 개발 진행 단계에 따라 순차적으로 생성되지만, 최종적인 디렉터리 구조는 아래 규칙을 따릅니다.

```
<USE_CASE_NAME>/
|-- adapters
|   |-- persistence
|   |   `-- <USE_CASE_NAME>Dao.java      (Repository 구현체)
|   `-- web
|       |-- <USE_CASE_NAME>Controller.java
|       `-- model
|           |-- <USE_CASE_NAME>Req.java  (Request DTO)
|           `-- <USE_CASE_NAME>Res.java  (Response DTO)
`-- application
    |-- <USE_CASE_NAME>UseCase.java      (Input Port - Interface)
    |-- <USE_CASE_NAME>Service.java      (UseCase 구현체)
    |-- <USE_CASE_NAME>PortOut.java      (Output Port - Interface)
    `-- model
        |-- <USE_CASE_NAME>Cmd.java      (Service 내부용 데이터)
```

---

# 🧪 테스트 케이스 작성 (TDD 지침)

사용자가 `TC` 명령어와 함께 대상 객체를 지정하면, 해당 계층의 특성에 맞는 테스트 코드를 작성합니다.

### 공통 원칙
- **DisplayName**: `@DisplayName("Scenario: <성공/실패> - <설명>")` 형식을 따릅니다.
- **BDD 스타일**: `Given - When - Then` 구조로 주석을 달아 구분합니다.
- **Mocking**: 하위 계층은 적극적으로 Mocking하여 현재 테스트 대상의 로직에만 집중합니다.
- **Assertion**: `AssertJ`를 사용합니다.
- **Mocking 라이브러리**: `Mockito`를 사용하며, Spring Boot 3.4+ 기준 `@MockitoBean`을 사용합니다. (`@MockBean` 사용 금지)

### 1. Web Layer 테스트 (`..adapters.web..`)
**목표:** HTTP 요청 매핑, 파라미터 검증, 응답 상태 코드, UseCase 호출 여부 검증
- **Base Class:** `app.module.api.support.WebMvcTestBase` 상속
- **Annotation:** `@WebMvcTest(TargetController.class)`
- **Mocking:** `UseCase` 인터페이스를 `@MockitoBean`으로 주입받습니다.
- **검증(Verify):** `Mockito.verify()`를 사용하여 컨트롤러가 `UseCase`를 올바른 파라미터로 호출했는지 검증합니다.
- **Security:** `@WithMockUser`를 사용하여 인증된 사용자를 시뮬레이션합니다.

```java
// 예시
@MockitoBean
private CreateUserUseCase createUserUseCase;

@Test
@DisplayName("Scenario: 성공 - 유효한 요청시 회원 생성 유스케이스가 호출된다")
void create_user_success() throws Exception {
    // Given
    CreateUserReq req = new CreateUserReq("user", "pass");
    
    // When
    mockMvc.perform(post("/users").content(json(req))...)
           .andExpect(status().isOk());
           
    // Then
    verify(createUserUseCase).operate(any(CreateUserCmd.class));
}
```

### 2. Application Layer 테스트 (`..application..`)
**목표:** 순수 비즈니스 로직, 예외 처리, 트랜잭션 흐름 검증
- **환경:** Spring Context 없는 순수 단위 테스트 권장 (속도 향상)
- **Annotation:** `@ExtendWith(MockitoExtension.class)`
- **Mocking:** `PortOut`(Repository Interface)을 `@Mock`(Mockito)으로 생성하여 주입합니다.
- **검증:** 반환값 검증 및 `PortOut` 호출 여부(`verify`)를 검증합니다.

### 3. Persistence Layer 테스트 (`..adapters.persistence..`)
**목표:** 실제 DB 쿼리 동작, 엔티티 매핑, 제약 조건 위반 검증
- **Base Class:** `app.module.api.support.DataJpaTestBase` 상속
- **Annotation:** `@Import`를 사용하여 필요한 DAO(Repository)만 로드합니다.
- **Mocking:** 없음. 실제 DB(H2 등)와 상호작용합니다.

### 4. 통합 테스트 (`ITC`)
사용자가 `ITC` 명령어를 사용할 때 작성합니다.
**목표:** 전체 빈(Bean)이 연결된 상태에서의 End-to-End 검증
- **Base Class:** `app.module.api.support.IntegrationTestBase` 상속
- **특징:** Mocking을 최소화하고 실제 빈을 사용합니다. 단, 외부 API 등 통제 불가능한 요소만 제한적으로 Mocking 합니다.

