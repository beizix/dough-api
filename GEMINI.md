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

- 탭 간격 및 코드 스타일은 프로젝트 루트의 .editorconfig 파일에 정의된 규칙을 따릅니다. (기본: 공백 2칸)
- TDD 사이클에 맞춰 **필요한 시점에 필요한 파일만** 생성하는 것을 원칙으로 합니다.
- 단, 헥사고날 아키텍처의 패키지 구조는 유지합니다.

# 네이밍 규칙 (Naming Convention)

- **유스케이스 패키지**: `VerbNoun` (동사+명사) 형태의 카멜 케이스를 사용합니다.
  - ⭕ Good: `retrieveProfile`, `updateProfile`, `registerUser`, `deleteManager`
- **어댑터 패턴 적용 (Adapters)**: 기술적인 역할보다는 아키텍처 역할을 강조합니다.
  - **Web Layer**: `...WebAdapter` (예: `UpdateProfileWebAdapter`)
  - **Persistence Layer**: `...PersistAdapter` (예: `UpdateProfilePersistAdapter`)
- **과거 분사형 결과 모델 (Result Models)**: 유스케이스 수행 결과를 담는 객체는 행위가 완료된 상태를 표현하도록 과거 분사형을 사용합니다.
  - ⭕ Good: `ProfileLoaded`, `ProfileUpdated`, `UserRegistered`, `ManagerDeleted`, `PasswordUpdated`
- **행위 중심 포트 (Output Ports)**: 출력 포트는 인터페이스 자체가 무엇을 하는지 드러내야 하며, `PortOut` 접미사를 지양합니다.
  - ⭕ Good: `LoadProfile`, `UpdateProfile`, `RegisterUser`, `DeleteManager`, `LoadPassword`

# Java 코딩 규칙

- 모든 객체 생성은 `new` 키워드를 사용한 **생성자 호출 방식**으로 통일합니다. (Lombok `@Builder` 지양)
- **Record Class**: DTO, Command, 도메인 불변 객체는 `record` 타입을 적극 활용합니다.
- **Spring Boot 4.0.1 특이 사항**: `@WebMvcTest` 사용 시 `import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;` 경로를 사용해야 합니다.
- **Enum 매핑**: Entity에서 필드로 Enum을 사용할 경우, DB의 네이티브 ENUM 타입 사용을 지양하고 확장성을 확보하기 위해 반드시 `@Enumerated(EnumType.STRING)`과 `@Column(columnDefinition = "VARCHAR(255)")`를 함께 선언합니다.

---

# 헥사고날 아키텍처 파일 구조

파일은 개발 진행 단계에 따라 순차적으로 생성되지만, 최종적인 디렉터리 구조는 아래 규칙을 따릅니다. **특히 엔티티와 리포지토리는 공유 패키지(`shared`)에서 관리합니다.**

```
io.dough.api/
|-- config
`-- useCases
    |-- shared (사용자, 매니저 등 전역 엔티티 공유)
    |   `-- adapters
    |       `-- out
    |           `-- persistence
    |               |-- entity
    |               |   `-- <Domain>Entity.java (모든 엔티티는 AuditEntity 상속 및 @SQLRestriction 추가)
    |               `-- repository
    |                   `-- <Domain>Repository.java
    `-- <verbNoun> (유스케이스 명칭 - 예: registerUser)
        |-- adapters
        |   |-- in (인바운드 어댑터 - 외부에서 들어오는 요청)
        |   |   `-- web
        |   |       |-- <VerbNoun>WebAdapter.java (예: RegisterUserWebAdapter)
        |   |       |-- <VerbNoun>Request.java (RequestBody)
        |   |       `-- <VerbNoun>Response.java (ResponseBody)
        |   `-- out (아웃바운드 어댑터 - 외부로 나가는 요청)
        |       |-- persistence
        |       |   `-- <VerbNoun>PersistAdapter.java (예: RegisterUserPersistAdapter)
        |       |-- jwt
        |       |   `-- <VerbNoun>JwtAdapter.java (예: IssueTokenJwtAdapter)
        |       `-- storage
        |           `-- <VerbNoun><Type>Adapter.java (예: StoreFileS3Adapter)
        |-- application
        |   |-- port
        |   |   |-- in
        |   |   |   |-- <VerbNoun>UseCase.java (입력 포트)
        |   |   |   |-- <VerbNoun>Cmd.java (유스케이스 커맨드)
        |   |   |   `-- <Noun><Status>.java (결과 모델 - 예: UserRegistered)
        |   |   `-- out
        |   |       |-- <Behavior>.java (출력 포트 - 예: RegisterUser, LoadProfile)
        |   |       `-- <Noun>.java (필요시 도메인 정보 전달 객체 - 예: Profile)
        |   `-- service
        |       `-- <VerbNoun>Service.java (서비스 구현체 - 예: RegisterUserService)
        `-- domain (복잡한 비즈니스 로직이 필요한 경우에만 생성)
            `-- model
                `-- <Domain>.java (도메인 모델 - 예: UploadableFile.java)
```

## 헥사고날 계층과 컴포넌트

### `config` (설정 계층)

가장 외부에 위치한 계층으로, 전역적인 애플리케이션 설정을 담당합니다. 이 계층은 다른 모든 계층(application, adapters 등)을 참조할 수 있습니다. 하지만 어떤 계층도 `config` 계층을 직접 참조해서는
안 됩니다. `config/` 패키지가 이 계층의 기본 경로입니다.

### `application` (애플리케이션의 핵심 - 절차 및 조율)

애플리케이션의 흐름을 제어하고 유스케이스를 구현하는 계층입니다. 외부 세계(프레임워크, UI, DB 등)에 대한 의존성이 없습니다.
**절대 원칙: Application Layer는 Web Layer(Req, Res)나 Persistence Layer(Entity, Dao)의 객체를 참조(Import)해서는 안 됩니다. 데이터 교환은 오직 `application` 또는 `domain` 에 정의된 객체로만 수행합니다.**

-   `application/port/in/`: **입력 포트(Input Port)**
  -   `...UseCase.java`: 애플리케이션을 구동하는 방법을 정의하는 인터페이스입니다.
  -   `...Cmd.java`: 유스케이스 실행에 필요한 데이터 객체입니다.
  -   `...Loaded`, `...Registered`: 유스케이스 수행 결과를 나타내는 과거 분사형 모델입니다.
  -   메서드 이름: 유즈케이스의 맥락은 클래스명에서 표현되니 단일 메서드로 구성된다면 `operate` 로 지정합니다.
  -   예: `RegisterUserUseCase.operate(RegisterUserCmd cmd)`

-   `application/port/out/`: **출력 포트(Output Port)**
  -   애플리케이션이 외부 세계(DB, 외부 API 등)와 소통하는 방법을 정의하는 인터페이스입니다.
  -   `PortOut` 접미사를 지양하고, 행위 중심의 이름을 사용합니다.
  -   메서드 이름은 유스케이스 맥락에 따라 `operate`, `load`, `save` 등을 사용합니다.
  -   예: `LoadProfile.operate(UUID userId)`, `UpdateProfile.operate(UpdateProfileCmd cmd)`

-   `application/service/`: **서비스 구현체**
  -   실제 비즈니스 로직의 **절차(Step)**를 수행하고 트랜잭션을 관리합니다.
  -   `UseCase` 인터페이스를 구현하고, 출력 포트 인터페이스를 호출하여 필요한 데이터를 주고받습니다.
  -   비즈니스 판단(Rule) 자체는 `domain` 모델에게 위임합니다.

### `domain` (비즈니스 핵심 로직 - 데이터와 규칙)

실제 비즈니스 규칙과 도메인 모델을 정의합니다. **데이터와 비즈니스 규칙을 담는 모델 객체들은 `domain/model` 패키지 하위에 위치시킵니다.**
**모든 유스케이스가 도메인 계층을 가질 필요는 없으며, 복잡한 비즈니스 규칙이나 상태를 가진 모델이 필요한 경우에만 선택적으로 생성합니다.**

-   `domain/model/`: **도메인 모델**
  -   비즈니스의 핵심 데이터와 규칙을 담는 모델 객체(`UploadableFile`, `Address` 등)가 위치합니다. 유스케이스 명칭과 일치할 필요는 없으며, 비즈니스 실체에 부합하는 명확한 이름을 사용합니다.
  -   **도메인 모델은 애플리케이션 계층의 객체(Port DTO, Command 등)를 참조해서는 안 됩니다.**

### `adapters` (외부 세계와의 연결)

`application`에 정의된 인터페이스를 구현하거나 호출하여, 특정 기술(웹, 데이터베이스 등)과 애플리케이션 핵심부를 연결합니다.

-   `adapters/in/web/`: **인바운드 웹 어댑터**
  -   HTTP 요청을 받아 처리하고 응답을 반환하는 역할을 합니다.
  -   `WebAdapter`는 `UseCase` 인터페이스를 주입받아 호출함으로써 비즈니스 로직 실행을 위임합니다.
  -   **DTO 위치**: 요청/응답에 사용되는 데이터 전송 객체(`Request`, `Response`)는 별도의 `model` 패키지 없이 `adapters/in/web` 패키지 바로 아래에 위치합니다.
  -   **Swagger 적용**: 모든 WebAdapter 클래스에는 `@Tag`, 메서드에는 `@Operation` 및 `@ApiResponse`, 요청 파라미터(RequestBody 포함)에는 `@Parameter` 어노테이션을 반드시 추가하여 API 문서를 자동화합니다.

-   `adapters/out/`: **아웃바운드 어댑터**
  -   데이터베이스, 외부 API, 파일 시스템 등 외부 세계와의 상호작용을 담당합니다.
  -   출력 포트 인터페이스를 구현하여 기술적인 세부 사항을 처리하고, 결과를 애플리케이션 내부 도메인 모델로 변환하여 반환합니다.
  -   `persistence/`: 데이터베이스 연동 및 영속성 관리
  -   `jwt/`, `storage/`, `ai/`: 기타 외부 인프라 서비스 연동

### `model` 정의 타입

- 모든 DTO 및 데이터 전달 객체는 **record** 타입을 적극 활용합니다.
  - **입력**: `<VerbNoun>Cmd` (유스케이스 입력), `<VerbNoun>Request` (웹 요청)
  - **출력**: `<Noun><Status>` (행위 완료 모델 - 예: `UserRegistered`), `<VerbNoun>Response` (웹 응답)

### 사용자 프롬프트 예시

사용자는 다음과 같은 방식으로 요청합니다.
```
[ 신규 기능 구현 요청: 헥사고날 아키텍처 기반 ]

**1. 유스케이스 정의:**
   - **유스케이스 이름 (verbNoun):** `retrieveUser`
   - **기능 설명:** 사용자 상세정보 조회

**2. 아키텍처 구조:**
   - **패키지 생성 위치:** `io.dough.api.useCases.user` 하위에 `<verbNoun>`에 해당하는 패키지를 생성하고 기능 구현을 진행.

**3. 엔드포인트 정보:**
   - **엔드포인트 (ENDPOINT):** `/api/v1/user/{id}`
   - **HTTP 메서드:** `GET`
   - **API 목적:** 클라이언트가 전달한 id 파라미터를 기반으로 사용자 정보를 조회해 상세 정보를 전달한다.
   - **Swagger 적용:** API의 목적과 파라미터(요청/응답)에 맞게 적절한 Swagger 어노테이션을 상세히 추가합니다.

**4. 데이터 처리:**
   - **영속성 계층:** `UserRepository.java` 를 사용하여 엔티티 상세 정보를 전달한다.
```
---

# 🧪 TDD 실행 지침 (tdd)

사용자가 `tdd` 키워드와 함께 구현할 기능을 입력하면, 아래의 **Outside-In TDD 사이클**에 따라 단계별로 구현을 진행합니다.

### 1. 단계별 실행 원칙
1.  **Red (실패)**:
    - 실패하는 테스트 코드를 먼저 작성합니다.
    - 이때, 존재하지 않는 클래스나 메서드를 사용하여 **컴파일 에러**가 발생하는 것도 '실패'의 일부입니다.
    - 컴파일 에러를 해결하기 위해 **최소한의 타입(Interface, Class, DTO)만 선언**하고, 내부 로직은 비워둡니다.
    - 테스트를 실행하여 기대하는 결과(Assertion)가 실패하는지 확인합니다.
2.  **Green (성공)**: 테스트를 통과시키기 위한 최소한의 **구현 코드**를 작성합니다.
3.  **Refactor (개선)**: 코드를 정돈하고 다음 계층(Application -> Persistence)으로 내려가며 1~2 과정을 반복합니다.

### 2. 진행 방식
- 한 번에 모든 계층의 코드를 만들지 않습니다.
- 각 단계가 끝날 때마다 사용자에게 테스트 성공 여부를 보고하고, 다음 단계(내부 계층 테스트 작성)로 진행할지 확인합니다.
- **네이밍 및 구조**: 반드시 `WebAdapter`, `PersistAdapter` 명칭을 사용하며, 엔티티와 리포지토리는 `useCases.shared` 패키지 하위에 생성합니다.

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
- **Security:** `@WithMockUser`는 사용하지 않습니다. (보안 필터가 제외된 상태로 테스트를 진행합니다)

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
**선택 사항입니다.** 사용자가 `ITC` 명령어를 입력했을 때만 작성합니다. TDD 의 기본 사이클(Red-Green-Refactor)에는 포함되지 않습니다.
**목적:** 전체 빈(Bean)이 연결된 상태에서의 End-to-End 검증
- **Base Class:** `app.module.api.support.IntegrationTestBase` 상속
- **특징:** Mocking을 최소화하고 실제 빈을 사용합니다. 단, 외부 API 등 통제 불가능한 요소만 제한적으로 Mocking 합니다.

