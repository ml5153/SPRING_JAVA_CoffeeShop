# ☕ CoffeeShop Ordering System

> 다수 서버 환경에서도 안정적으로 동작하는 커피숍 주문 시스템

---

## 📌 목차
- [ERD](#erd)
- [API 명세서](#api-명세서)
- [설계 의도](#설계-의도)
- [문제 해결 전략](#문제-해결-전략)
- [기술적 선택 이유](#기술적-선택-이유)
- [실행 방법](#실행-방법)

---

## ERD

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   members   │       │   orders    │       │    menus    │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id (PK)     │◄──┐   │ id (PK)     │  ┌───►│ id (PK)     │
│ balance     │   └───│ member_id   │  │    │ name        │
│ version     │       │ menu_id     │──┘    │ price       │
│ created_at  │       │ amount      │       │ created_at  │
│ modified_at │       │ ordered_at  │       │ modified_at │
└─────────────┘       └─────────────┘       └─────────────┘
```

### 테이블 설계 의도
- `orders.amount` : 주문 시점의 가격을 스냅샷으로 저장 → 이후 메뉴 가격이 변경되어도 주문 내역 정합성 유지
- `members.version` : 낙관적 락을 위한 버전 관리 컬럼
- `orders.ordered_at` : 인기 메뉴 7일 집계 쿼리를 위한 인덱스 컬럼

---

## API 명세서

### 1. 메뉴 목록 조회
```
GET /api/menus
```
**Response**
```json
{
  "success": true,
  "data": [
    { "menuId": 1, "name": "아메리카노", "price": 3000 },
    { "menuId": 2, "name": "카페라떼", "price": 4000 }
  ]
}
```

---

### 2. 포인트 충전
```
POST /api/members/{memberId}/charge
```
**Request**
```json
{ "amount": 10000 }
```
**Response**
```json
{
  "success": true,
  "data": { "memberId": 1, "balance": 15000 }
}
```
**예외**

| 상황 | HTTP Status | 코드 |
|---|---|---|
| 존재하지 않는 유저 | 404 | USER_NOT_FOUND |
| 충전 금액 0 이하 | 400 | INVALID_INPUT |

---

### 3. 주문/결제
```
POST /api/orders
```
**Request**
```json
{ "memberId": 1, "menuId": 3 }
```
**Response**
```json
{
  "success": true,
  "data": {
    "orderId": 10,
    "memberId": 1,
    "menuId": 3,
    "menuName": "카페라떼",
    "amount": 4000,
    "remainBalance": 11000,
    "orderedAt": "2025-05-06T10:30:00"
  }
}
```
**예외**

| 상황 | HTTP Status | 코드 |
|---|---|---|
| 존재하지 않는 유저 | 404 | USER_NOT_FOUND |
| 존재하지 않는 메뉴 | 404 | MENU_NOT_FOUND |
| 잔액 부족 | 400 | INSUFFICIENT_BALANCE |
| 동시 요청 충돌 | 409 | CONFLICT_REQUEST |

---

### 4. 인기 메뉴 조회
```
GET /api/menus/popular
```
**Response**
```json
{
  "success": true,
  "data": [
    { "rank": 1, "menuId": 3, "name": "카페라떼", "price": 4000, "orderCount": 152 },
    { "rank": 2, "menuId": 1, "name": "아메리카노", "price": 3000, "orderCount": 98 },
    { "rank": 3, "menuId": 5, "name": "바닐라라떼", "price": 4500, "orderCount": 74 }
  ]
}
```

---

### 공통 에러 응답
```json
{
  "success": false,
  "error": {
    "code": "USER_NOT_FOUND",
    "message": "존재하지 않는 계정입니다."
  }
}
```

---

## 설계 의도

### 도메인 분리
```
domain/
├── member/   → 포인트 충전
├── menu/     → 메뉴 조회, 인기 메뉴
└── order/    → 주문/결제
global/
├── common/   → ApiResponse, BaseEntity
├── error/    → CommonError, CommonException, GlobalExceptionHandler
└── config/   → DataInitializer
```
각 도메인이 독립적으로 책임을 갖도록 분리했습니다.  
비즈니스 로직(`charge`, `deduct`)은 Service가 아닌 Entity에 위치시켜 도메인 모델을 풍부하게 유지했습니다.

---

## 문제 해결 전략

### 동시성 문제
포인트 충전과 차감이 동시에 발생할 경우 잔액 정합성이 깨질 수 있습니다.

**선택: 낙관적 락 (Optimistic Lock)**
- `Member` 엔티티에 `@Version` 필드 적용
- 두 요청이 동시에 같은 Member를 수정하면 하나는 `ObjectOptimisticLockingFailureException` 발생
- 409 Conflict로 응답하여 클라이언트가 재시도하도록 유도

**낙관적 락을 선택한 이유**
- 커피 주문 특성상 동일 사용자의 동시 요청 빈도가 높지 않음
- 비관적 락(Pessimistic Lock)은 DB 레벨에서 row를 점유해 처리량이 떨어짐
- 낙관적 락은 충돌이 적은 환경에서 비관적 락 대비 성능상 유리

### 다수 서버 환경
- DB 레벨의 `@Version`을 사용하므로 서버 인스턴스가 몇 개든 동일하게 동작
- 서버 내 상태(메모리 캐시 등)에 의존하지 않음

### 데이터 일관성
- 주문/결제는 하나의 `@Transactional` 안에서 처리
- 포인트 차감 → 주문 저장 → 데이터 플랫폼 전송 순서로 진행
- 중간에 예외 발생 시 전체 롤백

---

## 기술적 선택 이유

| 기술 | 선택 이유 |
|---|---|
| Spring Boot 3.5 | 최신 LTS, Jakarta EE 지원 |
| JPA + MySQL | 관계형 데이터 특성상 적합, 트랜잭션 관리 용이 |
| `@Version` 낙관적 락 | 충돌 빈도가 낮은 환경에서 비관적 락 대비 성능 우위 |
| `amount` 스냅샷 저장 | 메뉴 가격 변경 시에도 주문 내역 정합성 유지 |
| `ordered_at` 인덱스 | 7일 인기 메뉴 집계 쿼리 성능 최적화 |
| Docker Compose | 로컬 환경 DB 세팅 일관성 확보 |

---

## 실행 방법

### 1. 환경변수 설정
```
# .env
DB_URL=jdbc:mysql://localhost:3306/coffeeshop
DB_USERNAME=user
DB_PASSWORD=password
```

### 2. MySQL 실행
```bash
docker compose up -d
```

### 3. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 4. 테스트 실행
```bash
./gradlew test
```