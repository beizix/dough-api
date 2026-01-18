# Vision API

모두를 위한 Vision API 프로젝트입니다.

## 🚀 개발 및 실행 가이드

이 프로젝트는 Gradle Wrapper(`gradlew`)를 사용하여 빌드 및 실행을 관리합니다.

### 1. 애플리케이션 실행
로컬 환경에서 애플리케이션을 구동하려면 아래 명령어를 실행하세요.
```bash
./gradlew bootRun
```

### 2. 테스트 실행

#### 전체 테스트 실행
모든 테스트 코드를 실행하고 결과를 확인합니다.
```bash
./gradlew test
```

#### 특정 테스트 케이스만 실행
특정 클래스 또는 메서드만 지정하여 테스트를 실행할 수 있습니다.
```bash
# 특정 테스트 클래스 실행
./gradlew test --tests io.vision.api.config.security.SecurityConfigTest

# 특정 패키지의 모든 테스트 실행
./gradlew test --tests io.vision.api.useCases.signup.*

# 특정 테스트 메서드 실행
./gradlew test --tests io.vision.api.config.security.SecurityConfigTest.access_public_endpoint_success
```

### 3. 코드 스타일 관리 (Spotless)
이 프로젝트는 Google Java Format을 준수합니다.

#### 코드 자동 포맷팅 적용
작성한 코드를 프로젝트 규칙에 맞게 자동으로 정돈합니다. 커밋 전에 실행하는 것을 권장합니다.
```bash
./gradlew spotlessApply
```

#### 포맷팅 규칙 준수 확인
코드를 수정하지 않고 규칙 위반 여부만 체크합니다.
```bash
./gradlew spotlessCheck
```
