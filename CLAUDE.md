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
├── common/
│   ├── api/ApiResponse.java
│   ├── config/MessageConfig.java
│   ├── domain/BaseEntity.java
│   └── exception/ErrorCode, GlobalExceptionHandler, WmsException, WmsStateException
├── master/                          # 기준정보
│   ├── controller/
│   │   ├── ItemController, StorageController, RackController
│   │   └── req/  *CreateReq, *SearchReq
│   ├── domain/
│   │   ├── Item, LoadType, Storage, Rack, RackId
│   │   ├── enums: BagType, CellState, ItemKind, ItemSize, ItemUnit
│   │   │         LuggageStatus, PalletType, RackSize, RackStatus
│   │   │         SidePosition, StorageKind
│   │   └── vo/RackAddress
│   ├── repository/
│   │   └── ItemRepository, LoadTypeRepository, StorageRepository, RackRepository
│   └── service/
│       ├── ItemService, StorageService, RackService
│       └── dto/  ItemDto, StorageDto, RackDto
├── inventory/                       # 재고관리
│   ├── domain/
│   │   ├── Stock, HoldOrder
│   │   └── vo/  StockStatus, ReservationStatus
│   ├── repository/StockRepository, HoldOrderRepository
│   └── service/InventoryService, FifoCandidateSelector
├── member/                          # 회원/인증 (스켈레톤)
├── out/                             # 출고 (스켈레톤)
└── NewWmsApplication.java
```

## 문서 관리
- 프로젝트 관련 모든 마크다운 문서는 별도 (`../docs/`)에서 관리
- 이 레포에는 소스코드만 포함

## 컨벤션
- 계층형 패키지: `controller > req`, `service > dto`, `domain > vo`
- Request DTO는 `*Req`, 서비스 DTO는 `*Dto` 접미사
- DDD 지향: 도메인 로직은 domain 패키지 엔티티에 배치
- TDD 지향: 기능 구현 시 테스트 작성 병행
- Rack 위치는 `(storage_id, rack_no)` 복합키로 식별 — rack_no는 Storage 내에서만 유일

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
- **RackAddress VO**: 자동창고 전용 — `SSZZXXYY` 포맷 파싱, 비자동창고(대기장 등)에는 미적용
