# Configuration Guide

이 문서는 Vision API 프로젝트의 주요 설정 항목과 환경 구성 방법을 설명합니다.

## 1. 인증 및 JWT 설정 (Security & JWT)

애플리케이션의 보안 인증과 토큰 관리를 위한 설정입니다.

### 1.1 JWT (JSON Web Token)

| 프로퍼티 | 타입 | 기본값 | 설명 |
| :--- | :--- | :--- | :--- |
| `jwt.secret` | String | (필수) | JWT 서명에 사용할 비밀 키 (최소 32자 이상 권장) |
| `jwt.access-token-validity` | Long | 3600000 | Access Token 유효 시간 (밀리초, 기본 1시간) |
| `jwt.refresh-token-validity` | Long | 86400000 | Refresh Token 유효 시간 (밀리초, 기본 24시간) |

---

## 2. 데이터베이스 설정 (Database)

애플리케이션의 데이터 영속성을 위한 데이터베이스 연결 설정입니다.

### 2.1 H2 데이터베이스 (로컬/테스트)

로컬 개발 환경에서는 별도의 설치 없이 H2 인메모리 또는 파일 기반 데이터베이스를 사용합니다.

- **JDBC URL**: `jdbc:h2:file:~/h2/vision/vision;AUTO_SERVER=TRUE`
- **Driver**: `org.h2.Driver`
- **Username**: `sa`
- **Password**: (없음)

---

## 3. 파일 저장소 전략 (File Storage Strategy)

애플리케이션은 파일 저장 시 로컬 서버 저장소 또는 AWS S3 저장소 중 하나 이상을 선택적으로 활성화할 수 있습니다. 각 전략은 `app.storage` 프로퍼티를 통해 제어됩니다.

### 3.1 활성화 설정

| 프로퍼티 | 타입 | 기본값 | 설명 |
| :--- | :--- | :--- | :--- |
| `app.storage.local.enabled` | Boolean | `true` | 로컬 서버 저장소 사용 여부 |
| `app.storage.s3.enabled` | Boolean | `false` | AWS S3 저장소 사용 여부 |

> **Tip**: 두 설정을 모두 `true`로 설정하면 로컬 저장소와 S3 저장소를 동시에 사용할 수 있습니다. 이 경우 애플리케이션 내에서 비즈니스 로직(예: 파일 타입이나 유스케이스)에 따라 저장 위치를 동적으로 선택할 수 있습니다.

### 3.2 로컬 저장소 설정 (Local Storage)

`app.storage.local.enabled`가 `true`인 경우, 다음 설정이 필수적으로 요구됩니다.

- **`app.upload.path`**: 파일이 저장될 물리적인 디렉토리 경로입니다.
- **`app.upload.tmpDir`**: 멀티파트 파일 처리 시 사용할 임시 저장 경로입니다.

### 3.3 S3 저장소 설정 (AWS S3)

`app.storage.s3.enabled`가 `true`인 경우, `spring.cloud.aws` 하위의 AWS 자격 증명 및 S3 버킷 설정이 필요합니다. 상세 설정은 `application.yml`의 `cloud.aws` 섹션을 참조하십시오.

### 3.4 전략 전환 가이드

#### 로컬 모드 (기본)
아무런 설정을 하지 않으면 로컬 저장소 모드로 동작하며, 사용자의 홈 디렉토리(`~/vision/upload`)를 기본 경로로 사용합니다.

#### S3 전용 모드
S3만 사용하고 로컬 저장소 관련 빈(Bean)과 경로 검증을 비활성화하려면 다음과 같이 실행합니다.

```bash
java -jar vision-api.jar \
  -Dapp.storage.local.enabled=false \
  -Dapp.storage.s3.enabled=true \
  -Dspring.cloud.aws.credentials.access-key=... \
  -Dspring.cloud.aws.credentials.secret-key=... \
  -Dspring.cloud.aws.s3.bucket=...
```
