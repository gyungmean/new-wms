# CLAUDE.md — new-wms

개인 학습 프로젝트: 레거시 프로시저 기반 WMS 출고 시스템을 현대적인 Spring Boot + DDD/TDD로 재설계

## 기술 스택
- Java 17, Spring Boot 3.5, Gradle
- Spring Data JPA, H2 (개발용 DB)
- Lombok, Bean Validation
- JUnit 5

## 패키지 구조
```
dev.gyungmean.newwms
├── member/          # 회원/인증
│   ├── controller/  # LoginController, req/LoginReq
│   └── service/     # LoginService, dto/LoginDto
├── out/             # 출고
│   ├── controller/  # req/OutPlanOrderReq, OutPlanSearchReq
│   └── domain/      # Car, Container, Driver, OutPlanMaster
└── NewWmsApplication.java
```

## 문서 관리
- 프로젝트 관련 모든 마크다운 문서는 `docs/` 폴더에 작성
- 계획, 도메인 설명, 코드 캐시, 작업 로그 등 모든 md 파일의 기본 경로: `docs/`

## 컨벤션
- 계층형 패키지: `controller > req`, `service > dto`, `domain`
- Request DTO는 `*Req`, 서비스 DTO는 `*Dto` 접미사
- DDD 지향: 도메인 로직은 domain 패키지 엔티티에 배치
- TDD 지향: 기능 구현 시 테스트 작성 병행

## 빌드 & 테스트
```bash
./gradlew build        # 빌드
./gradlew test         # 테스트
./gradlew bootRun      # 실행
```

## 주요 도메인 개념
- **출고 오더**: OutPlanMaster 중심의 출고 계획 관리
- **재고 선택 전략**: FIFO
- **Twin 작업**: 출고 작업 병합 판단
- **자동 랙이동**: 출고 효율화를 위한 자동 결정
