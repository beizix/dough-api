# ADR 003: 다중 저장소 지원 및 파일 URL 조회를 위한 전략 패턴 적용

## 상태
준비(Proposed) -> **승인(Accepted)**

## 컨텍스트
본 시스템은 이미지, 문서 등 다양한 파일을 처리하며, 인프라 환경(로컬 개발, 클라우드 운영)에 따라 서로 다른 저장소 기술(Local File System, AWS S3 등)을 지원해야 합니다.
또한, 저장된 파일을 클라이언트가 참조할 수 있도록 URL을 제공할 때도 각 저장소의 기술적 특성(Static Mapping, Pre-signed URL 등)에 맞는 URL 생성 로직이 필요합니다.

애플리케이션 코어가 특정 저장 기술에 종속되지 않으면서도 유연하게 확장 가능한 구조를 설계해야 합니다.

## 결정
인터페이스와 전략 패턴(Strategy Pattern)을 활용하여 파일 저장 및 URL 생성 로직을 추상화합니다.

### 1. 파일 저장 전략 (SaveToFileStoragePortOut)
- `SaveFileService`는 `Set<SaveToFileStoragePortOut>`을 주입받아 관리합니다.
- `FileUploadType`에 정의된 `FileStorageType`(LOCAL, S3 등)에 따라 런타임에 적절한 저장 전략을 선택합니다.
- **인터페이스**: `SaveToFileStoragePortOut`
- **구현체**: `SaveToLocalStorageAdapter`, `SaveToS3StorageAdapter`

### 2. 메타데이터 기록 포트 (SaveFileMetadataPortOut)
- 물리적 파일 저장이 성공적으로 완료된 후, 파일의 위치와 식별 정보를 DB에 기록합니다.
- **역할**: 영속성 계층(Persistence Layer)과의 인터페이스로, `FileMetadataEntity`를 생성하고 저장합니다.
- **정합성 유지**: 서비스 레이어에서 물리 저장이 예외 없이 종료된 시점에만 호출되어, "파일은 있지만 DB 정보는 없는" 상황을 방지합니다.

### 3. URL 생성 전략 (GetFileURLPortOut)
- `GetFileURLService`는 `Set<GetFileURLPortOut>`을 주입받아 관리합니다.
- 메타데이터에 기록된 저장소 타입 정보를 바탕으로 적절한 URL 생성 전략을 선택합니다.
- **인터페이스**: `GetFileURLPortOut`
- **구현체**: `GetLocalFileURLAdapter`, `GetS3FileURLAdapter`

### 3. 처리 흐름
1.  **저장 시**: `SaveFileUseCase` 호출 -> 유효성 검사 -> 저장 전략 선택 및 물리 파일 저장 -> 메타데이터 DB 기록
2.  **조회 시**: `GetFileURLUseCase` 호출 -> 메타데이터 조회 -> URL 생성 전략 선택 -> 접근 가능한 URL 반환

## 결과

### 긍정적 영향
- **유연한 확장성**: 새로운 저장소(예: Google Cloud Storage)가 추가되더라도 기존 비즈니스 로직 수정 없이 새로운 어댑터만 추가하여 확장이 가능합니다. (OCP 준수)
- **테스트 용이성**: 인프라 종속적인 부분을 Mocking 하거나 별도의 테스트 전략으로 대체하기 쉽습니다.
- **단일 책임 원칙(SRP)**: 서비스는 비즈니스 규칙(파일 유효성, 경로 생성 등)에만 집중하고, 물리적인 입출력은 어댑터가 담당합니다.

### 부정적 영향
- **복잡도 증가**: 단순한 파일 시스템 저장에 비해 클래스와 인터페이스의 수가 늘어납니다.
- **전략 선택 비용**: 런타임에 리스트를 순회하여 적절한 전략을 찾는 미세한 오버헤드가 발생합니다. (하지만 체감할 수준은 아님)
