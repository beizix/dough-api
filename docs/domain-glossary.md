# 도메인 용어 사전 (Domain Glossary)

이 문서는 프로젝트 내에서 사용하는 핵심 용어들의 정의와 비즈니스 규칙을 담고 있습니다. 모든 코드의 네이밍과 의사소통은 이 사전을 따릅니다.

## 1. 신원 및 권한 관리 (Identity & Access)

| 용어 (English) | 용어 (Korean) | 정의 및 비즈니스 규칙 | 관련 클래스/패키지 |
| :--- | :--- | :--- | :--- |
| **User** | 사용자 | 시스템을 이용하는 주체. 이메일(`email`)을 고유 식별자로 가집니다. | `UserEntity`, `LoginUser`, `SignupUser` |
| **Role** | 역할 | 사용자의 권한 그룹. `USER`, `MANAGER`, `ADMIN` 등으로 구분됩니다. | `Role` (Enum) |
| **Privilege** | 특권 | 역할 내에 포함된 더 세부적인 실행 권한 단위입니다. | `Privilege` (Enum) |
| **AuthToken** | 인증 토큰 | JWT 기반의 보안 토큰 세트. `AccessToken`과 `RefreshToken`으로 구성됩니다. | `AuthToken` |
| **ManageAuthToken** | 인증 토큰 관리 | 토큰의 생성, 검증, 갱신, 정보 추출을 아우르는 일련의 행위입니다. | `ManageAuthTokenUseCase` |

## 2. 파일 관리 (File Management)

| 용어 (English) | 용어 (Korean) | 정의 및 비즈니스 규칙 | 관련 클래스/패키지 |
| :--- | :--- | :--- | :--- |
| **SaveFile** | 파일 저장 | 파일을 물리 저장소에 보관하고 메타데이터를 DB에 기록하는 전체 프로세스입니다. | `SaveFileUseCase` |
| **FileMetadata** | 파일 메타데이터 | 파일의 UUID, 저장 경로, 물리 파일명 등 DB에 기록되는 논리적 정보입니다. | `FileMetadataEntity`, `SaveFileMetadata` |
| **FileUploadType** | 파일 업로드 유형 | 업로드 목적에 따른 저장소 위치 및 허용 확장자 정책 묶음입니다. | `FileUploadType` (Enum) |
| **FileStorageType** | 저장소 기술 유형 | `LOCAL`(로컬 디스크), `S3`(AWS) 등 물리적 저장 기술의 종류를 나타냅니다. | `FileStorageType` (Enum) |
| **AcceptableFileType** | 허용 파일 유형 | 각 파일 카테고리(이미지, 엑셀 등)별로 허용되는 확장자와 MIME 타입의 정의입니다. | `AcceptableFileType` (Enum) |
| **GetFileURL** | 파일 URL 조회 | 저장된 파일에 접근할 수 있는 경로(URL)를 생성하거나 조회하는 행위입니다. | `GetFileURLUseCase` |

## 3. 아키텍처 공통 (Architecture Common)

| 용어 (English) | 용어 (Korean) | 정의 및 비즈니스 규칙 | 관련 접미사 |
| :--- | :--- | :--- | :--- |
| **UseCase** | 유스케이스 | 사용자의 목표를 달성하는 하나의 비즈니스 실행 단위 (입력 포트). | `...UseCase` |
| **PortOut** | 출력 포트 | 애플리케이션 코어가 외부(DB, 저장소)에 요구하는 인터페이스. | `...PortOut` |
| **Cmd (Command)** | 커맨드 | 유스케이스 실행에 필요한 입력 데이터 묶음 (Value Object). | `...Cmd` |
| **Adapter** | 어댑터 | 외부 기술(Web, JPA, S3)과 포트를 연결하는 물리적 구현체. | `...WebAdapter`, `...PersistAdapter` |
