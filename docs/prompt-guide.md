# AI 프롬프트 가이드 (AI Prompt Guide)

이 문서는 AI(Gemini CLI 등)를 활용하여 헥사고날 아키텍처 기반의 신규 기능을 구현할 때, 최적의 결과물을 얻기 위한 프롬프트 작성 요령과 상황별 예시를 담고 있습니다.

---

## 1. 핵심 원칙 (Core Principles)

모든 기능 구현 요청 시 다음 3가지 원칙을 프롬프트에 명시하는 것이 좋습니다.
1.  **Outside-In TDD**: 웹 계층 테스트 → 서비스 계층 테스트 → 영속성 계층 테스트 순으로 진행.
2.  **Hexagonal Architecture**: 의존성 역전 원칙(DIP)을 준수하며, 도메인이 프레임워크에 의존하지 않도록 강제.
3.  **Step-by-Step**: 한 번에 모든 코드를 작성하지 말고, 각 계층의 테스트 성공 여부를 확인하며 단계별로 진행.

---

## 2. 상황별 프롬프트 예시 (CRUD)

### 2.1 단건 생성 (Create)
데이터 유효성 검증과 가입/등록 직후의 추가 행위(토큰 발급 등)가 포함된 요청 예시입니다.

```markdown
[ 신규 기능 구현 요청: 헥사고날 TDD 기반 ]

**1. 유스케이스 정의**
- **이름 (USE_CASE_NAME):** CreateManager
- **설명:** 매니저 생성 기능

**2. 아키텍처 및 패키지 구조**
- **생성 위치:** io.dough.api.useCases.user.maintenance 하위
- **원칙:** 헥사고날 아키텍처 준수 (adapters.web, adapters.persistence, application, domain 계층 분리)
- **공유 자원:** shared 패키지의 엔티티 및 리포지토리 활용

**3. 엔드포인트 정보 (Web Adapter)**
- **URL / Method:** `POST` `/api/v1/manager/users/manager`
- **Swagger Tag:** `사용자 관리`
- **요청 바디:** email (String), displayName (String), password (String)

**4. 계층별 세부 구현 요구사항**
- **TDD (Outside-In):**
  - WebAdapterTest (MockMvc 기반, 컴파일 에러 해결 및 UseCase 호출 검증)
  - ServiceTest (순수 단위 테스트, Output Port 호출 검증)
  - PersistAdapterTest (DataJpaTest 기반, 실제 DB 연동 검증)
- **도메인 계층 (Domain):**
  - Command: email (String), displayName (String), password (String), 그리고 role (Role) 속성은 Role.MANAGER 를 고정으로 가짐.
  - Model: 상세 항목(id, email, displayName, role, createdAt) 속성을 담은 결과 객체 정의.
- **애플리케이션 계층 (Application):**
  - 입력 포트(UseCase) 및 출력 포트(PortOut) 인터페이스 정의.
  - 서비스에서 포트를 통한 흐름 제어
- **영속성 계층 (Persistence):**
  - UserRepository.java 을 이용한 데이터 조회


**5. 최종 검증**
- 전체 테스트 케이스 실행 및 패스 확인.
```

### 2.2 단건 조회 (READ)
```markdown
[ 신규 기능 구현 요청: 헥사고날 아키텍처 기반 ]

**1. 유스케이스 정의:**
   - **유스케이스 이름 (USE_CASE_NAME):** `GetUsers`
   - **기능 설명:** 사용자 목록 관리

**2. 아키텍처 구조:**
   - **패키지 생성 위치:** `io.dough.api.useCases.user.maintenance` 하위에 `<USE_CASE_NAME>`에 해당하는 패키지를 생성하고 기능 구현을 진행.

**3. 엔드포인트 정보:**
   - **@Tag:** `사용자 관리`
   - **엔드포인트 (ENDPOINT):** {"/api/v1/manager/users"}
   - **HTTP 메서드:** `GET`
   - **API 목적:** 클라이언트에게 페이징 처리된 사용자 목록을 전달한다. 클라이언트가 전달하는 필터링 속성(email, displayName, role)을 기반으로 사용자 목록을 전달한다.
   - **Swagger 적용:** API의 목적과 파라미터(요청/응답)에 맞게 적절한 Swagger 어노테이션을 상세히 추가합니다.

**4. 요구사항:**
   - **TDD**: TDD 방식으로 `웹`, `서비스`, `영속성` 계층에 대한 실패하는 테스트 케이스를 먼저 작성한다.
   - **웹 계층:** email, displayName, role 필터링 조건을 받는다. Pageable 을 인자로 선언해서 정렬 및 페이지네이션 속성을 받는다.
   - **도매인 계층:** email, displayName, role, 그리고 페이징 관련 정보를 담는 속성을 전달 받는 커맨드 객체를 생성한다. 반환 객체는 id, email, displayName, role 속성으로 구성된 객체의 배열을 담은 속성을 갖는다. 그리고, 페이징 관련 정보를 담은 별도 속성도 필요하다.
   - **서비스 계층:** 커맨드 객체를 기반으로 영속성 레이어를 호출한다.
   - **영속성 계층:** `UserRepository.java` 를 사용하고  Specification 을 사용하여 데이터 처리 작업을 진행한다. email 과 displayName 은 like 검색으로 수행한다.

**5. 검증:**
   - 전체 테스트 케이스 실행 및 패스 확인.
```

### 2.3 단건 수정 (UPDATE)
```markdown
[ 신규 기능 구현 요청: 헥사고날 TDD 기반 ]

**1. 유스케이스 정의**
- **이름 (USE_CASE_NAME):** UpdateManager
- **설명:** 매니저 정보 수정 기능

**2. 아키텍처 및 패키지 구조**
- **생성 위치:** io.dough.api.useCases.user.maintenance 하위
- **원칙:** 헥사고날 아키텍처 준수 (adapters.web, adapters.persistence, application, domain 계층 분리)
- **공유 자원:** shared 패키지의 엔티티 및 리포지토리 활용

**3. 엔드포인트 정보 (Web Adapter)**
- **URL / Method:** `PATCH` `/api/v1/manager/users/manager`
- **Swagger Tag:** `사용자 관리`
- **요청 바디:** email (String), displayName (String), password (String)

**4. 계층별 세부 구현 요구사항**
- **TDD (Outside-In):**
  - WebAdapterTest (MockMvc 기반, 컴파일 에러 해결 및 UseCase 호출 검증)
  - ServiceTest (순수 단위 테스트, Output Port 호출 검증)
  - PersistAdapterTest (DataJpaTest 기반, 실제 DB 연동 검증)
- **도메인 계층 (Domain):**
  - Command: email (String), displayName (String), password (String)
  - Model: 상세 항목(id, email, displayName, role, updatedAt) 속성을 담은 결과 객체 정의.
- **애플리케이션 계층 (Application):**
  - 입력 포트(UseCase) 및 출력 포트(PortOut) 인터페이스 정의.
  - 서비스에서 포트를 통한 흐름 제어
- **영속성 계층 (Persistence):**
  - UserRepository.java 을 이용한 데이터 조회


**5. 최종 검증**
- 전체 테스트 케이스 실행 및 패스 확인.
```

### 2.4 단건 삭제 (DELETE)
```markdown
[ 신규 기능 구현 요청: 헥사고날 TDD 기반 ]

**1. 유스케이스 정의**
- **이름 (USE_CASE_NAME):** RemoveManager
- **설명:** 매니저 삭제 기능

**2. 아키텍처 및 패키지 구조**
- **생성 위치:** io.dough.api.useCases.user.maintenance 하위
- **원칙:** 헥사고날 아키텍처 준수 (adapters.web, adapters.persistence, application, domain 계층 분리)
- **공유 자원:** shared 패키지의 엔티티 및 리포지토리 활용

**3. 엔드포인트 정보 (Web Adapter)**
- **URL / Method:** `DELETE` `/api/v1/manager/users/manager`
- **Swagger Tag:** `사용자 관리`
- **요청 바디:** id (String)

**4. 계층별 세부 구현 요구사항**
- **TDD (Outside-In):**
  - WebAdapterTest (MockMvc 기반, 컴파일 에러 해결 및 UseCase 호출 검증)
  - ServiceTest (순수 단위 테스트, Output Port 호출 검증)
  - PersistAdapterTest (DataJpaTest 기반, 실제 DB 연동 검증)
- **도메인 계층 (Domain):**
  - Command: id (UUID)
  - Model: 삭제 여부 boolean 속성과 deletedAt 을 담은 결과 객체 정의.
- **애플리케이션 계층 (Application):**
  - 입력 포트(UseCase) 및 출력 포트(PortOut) 인터페이스 정의.
  - 서비스에서 포트를 통한 흐름 제어
- **영속성 계층 (Persistence):**
  - UserRepository.java 을 이용한 soft delete 구현


**5. 최종 검증**
- 전체 테스트 케이스 실행 및 패스 확인.
```

### 2.5 목록 조회 (List / Search)
페이징 처리와 동적 필터링(`Specification`)이 포함된 목록 조회 요청 예시입니다.

```markdown
[ 신규 기능 구현 요청: 헥사고날 TDD 기반 ]

**1. 유스케이스 정의**
- **이름 (USE_CASE_NAME):** GetUsers
- **설명:** 사용자 목록 관리 및 조건 검색 기능

**2. 아키텍처 및 패키지 구조**
- **생성 위치:** io.dough.api.useCases.user.maintenance 하위
- **원칙:** 헥사고날 아키텍처 준수 (adapters.web, adapters.persistence, application, domain 계층 분리)
- **공유 자원:** shared 패키지의 엔티티 및 리포지토리 활용

**3. 엔드포인트 정보 (Web Adapter)**
- **URL / Method:** `GET` `/api/v1/manager/users`
- **Swagger Tag:** `사용자 관리`
- **필터 파라미터:** email (String), displayName (String), role (Enum)
- **페이징:** Pageable을 통한 자동 바인딩 처리

**4. 계층별 세부 구현 요구사항**
- **TDD (Outside-In):**
  - WebAdapterTest (MockMvc 기반, 컴파일 에러 해결 및 UseCase 호출 검증)
  - ServiceTest (순수 단위 테스트, Output Port 호출 검증)
  - PersistAdapterTest (DataJpaTest 기반, 실제 DB 연동 및 Specification 검증)
- **도메인 계층 (Domain):**
  - Command: record 타입. 외부 프레임워크(Pageable) 의존성 없이 기본 타입 필드로 구성.
  - Model: 목록 항목(UserForList (email, displayName, role))과 페이징 메타데이터(PageInfo)를 포함하는 결과 객체 정의.
- **애플리케이션 계층 (Application):**
  - 입력 포트(UseCase) 및 출력 포트(PortOut) 인터페이스 정의.
  - 서비스에서 포트를 통한 흐름 제어 및 트랜잭션 관리 (readOnly = true).
- **영속성 계층 (Persistence):**
  - UserRepository.java에 JpaSpecificationExecutor 적용.
  - Specification을 활용하여 동적 쿼리 구현.
  - 검색 조건: email과 displayName은 LIKE (%keyword%) 검색, role은 Equal 검색 적용.


**5. 최종 검증**
- 전체 테스트 케이스 실행 및 패스 확인.
```
