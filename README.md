# 🍺 ZZAN (짠!) - 술집 예약 시스템

> 대학가 술집 실시간 예약 플랫폼 — 빈자리 확인부터 예약까지 한 번에

| **Frontend** | **Backend** |
|:---:|:---:|
| [zzan-frontend]() | [zzan-backend](https://github.com/yxzkng/zzan.git) |

<br>

## 📌 프로젝트 소개

**ZZAN**은 대학교 주변 술집의 예약 현황을 실시간으로 확인하고, 간편하게 예약할 수 있는 서비스입니다.

- 날짜와 인원수만 입력하면 **예약 가능한 술집**을 바로 확인
- **시간대별 잔여 좌석**을 한눈에 파악
- 클릭 몇 번으로 **예약 신청 & 취소** 완료

<br>

## 🛠 기술 스택

| 구분 | 기술 |
|------|------|
| **Language** | Java 21 |
| **Framework** | Spring Boot 4.1.0 |
| **ORM** | Spring Data JPA (Hibernate) |
| **Database** | H2 (개발) / MySQL (운영) |
| **Validation** | Jakarta Bean Validation |
| **Build** | Gradle |
| **Deploy** | DuckDNS + Custom Server |
| **Frontend** | Vercel |

<br>

## 🗂 프로젝트 구조

```
src/main/java/likelion/demo/
│
├── auth/                           # 🔐 인증 모듈
│   ├── controller/AuthController
│   ├── service/AuthService
│   ├── repository/MemberRepository
│   ├── entity/Member, Role
│   └── dto/request & response
│
├── store/                          # 🏪 술집 조회 모듈
│   ├── controller/StoreController
│   ├── service/StoreService
│   ├── repository/StoreRepository
│   ├── entity/Store
│   └── dto/StoreListResponse, TimeslotResponse
│
├── reservation/                    # 📋 예약 모듈
│   ├── controller/ReservationController
│   ├── service/ReservationService
│   ├── repository/ReservationRepository
│   ├── entity/Reservation, ReservationStatus
│   └── dto/request & response
│
├── global/                         # 🌐 공통
│   ├── common/ApiResponse
│   └── exception/GlobalExceptionHandler + 커스텀 예외들
│
└── WebConfig                       # CORS 설정
```

<br>

## 📊 ERD

```
┌──────────────┐       ┌──────────────────┐       ┌──────────────┐
│   Member     │       │   Reservation    │       │    Store     │
├──────────────┤       ├──────────────────┤       ├──────────────┤
│ id (PK)      │──┐    │ id (PK)          │    ┌──│ id (PK)      │
│ loginId (UQ) │  └───>│ member_id (FK)   │    │  │ name         │
│ password     │       │ store_id (FK)    │<───┘  │ address      │
│ phoneNumber  │       │ date             │       │ openTime     │
│ role         │       │ startTime        │       │ closeTime    │
│ createdAt    │       │ endTime          │       │ seatCapacity │
└──────────────┘       │ headcount        │       │ closedDays   │
                       │ status           │       └──────────────┘
                       │ createdAt        │
                       └──────────────────┘
```

**관계**: `Member (1) ── (N) Reservation (N) ── (1) Store`

<br>

## 🔗 API 명세

### 인증 API

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| `POST` | `/api/auth/signup` | 회원가입 (STUDENT / OWNER) | X |
| `POST` | `/api/auth/login` | 로그인 (세션 발급) | X |
| `POST` | `/api/auth/logout` | 로그아웃 (세션 만료) | O |

### 술집 API

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| `GET` | `/api/stores?date={date}&headcount={n}` | 예약 가능한 술집 목록 조회 | O |
| `GET` | `/api/stores/{id}/timeslots?date={date}&headcount={n}` | 시간대별 잔여 좌석 조회 | O |

### 예약 API

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| `POST` | `/api/reservations` | 예약 신청 | O |
| `GET` | `/api/reservations/me` | 내 예약 목록 조회 | O |
| `PATCH` | `/api/reservations/{id}/cancel` | 예약 취소 | O |

<br>

## 💡 핵심 구현 사항

### 1. 실시간 좌석 가용성 계산
```
잔여 좌석 = 전체 좌석 - 해당 시간대 예약 인원 합계
```
- 1시간 단위 타임슬롯으로 분할하여 시간대별 잔여 좌석 계산
- 취소된 예약(`CANCELED`)은 계산에서 제외

### 2. 예약 충돌 방지
- 예약 신청 시 요청한 시간 범위 내 **모든 슬롯**의 잔여 좌석 검증
- 하나라도 초과 시 `409 Conflict` 반환

### 3. N+1 쿼리 최적화
- `fetch join`을 활용하여 내 예약 목록 조회 시 Store 정보를 한 번에 로딩
- Store 목록 조회 시 `closedDays` 컬렉션도 `LEFT JOIN FETCH`로 즉시 로딩

### 4. 비즈니스 검증 로직
- 과거 날짜 예약 차단
- 영업시간 외 예약 차단
- 휴무일 예약 차단
- 본인 예약만 취소 가능 (403 Forbidden)

<br>

## 🔒 인증 방식

- **세션 기반 인증** (HTTP Session)
- 로그인 시 `LOGIN_MEMBER_ID`를 세션에 저장
- 인증이 필요한 API는 세션 검증 후 처리
- 로그아웃 시 세션 무효화

<br>

## 📦 응답 형식

모든 API는 통일된 응답 포맷을 사용합니다:

```json
{
  "success": true,
  "code": 200,
  "message": "요청 성공 메시지",
  "data": { ... }
}
```

### 에러 응답

| 상태 코드 | 예외 | 상황 |
|-----------|------|------|
| `400` | IllegalArgumentException | 잘못된 입력 (과거 날짜, 시간 범위 오류 등) |
| `401` | UnauthorizedException | 로그인 필요 |
| `403` | ForbiddenException | 타인의 예약 취소 시도 |
| `404` | NotFoundException | 존재하지 않는 리소스 |
| `409` | ConflictException | 좌석 부족 / 아이디 중복 |

<br>

## 🏪 초기 데이터 (data.sql)

| 술집 | 위치 | 영업시간 | 좌석 | 휴무일 |
|------|------|----------|------|--------|
| 낭만오지 | 정문 도보 1분 | 17:00 ~ 24:00 | 12석 | - |
| 캠퍼스 맥주창고 | 후문 도보 3분 | 17:00 ~ 24:00 | 8석 | 월요일 |
| 술이야기 | 정문 도보 5분 | 18:00 ~ 24:00 | 20석 | 일요일 |
| 달빛포차 | 후문 도보 1분 | 19:00 ~ 24:00 | 6석 | 월, 화 |
| 한잔의 여유 | 정문 도보 10분 | 16:00 ~ 24:00 | 15석 | - |


<br>

## 👥 Contributors


<br>

---

<p align="center">
  <b>멋쟁이사자처럼 복커톤 </b> | Built with Spring Boot
</p>
