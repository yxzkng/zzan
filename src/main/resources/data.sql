-- 1. User (회원 공통)
-- 'User'는 DB 시스템 예약어일 수 있으므로 보통 'users'로 복수형을 사용하거나 백틱(`)으로 감싸는 것을 권장합니다.
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       login_id VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(50),
                       role VARCHAR(20) NOT NULL,     -- Enum: STUDENT, OWNER
                       created_at DATETIME NOT NULL
);

-- 2. Store (술집)
CREATE TABLE store (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       owner_id BIGINT NOT NULL UNIQUE, -- ⭐️ OneToOne 관계이므로 한 명의 사장님이 하나의 가게만 가질 수 있도록 UNIQUE 제약조건 추가
                       name VARCHAR(255) NOT NULL,
                       address VARCHAR(255),
                       latitude DOUBLE,
                       longitude DOUBLE,
                       description TEXT,
                       open_time TIME,
                       close_time TIME,
                       slot_capacity INT NOT NULL,

    -- 외래키(FK) 설정
                       FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Reservation (예약)
CREATE TABLE reservation (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             student_id BIGINT NOT NULL,
                             store_id BIGINT NOT NULL,
                             reservation_date DATE NOT NULL,
                             start_time TIME NOT NULL,
                             end_time TIME NOT NULL,
                             headcount INT NOT NULL,
                             status VARCHAR(20) NOT NULL,   -- Enum: REQUESTED, CONFIRMED, CANCELED
                             created_at DATETIME NOT NULL,

    -- 외래키(FK) 설정
                             FOREIGN KEY (student_id) REFERENCES users(id),
                             FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE
);

-- 4. Holiday (휴무일)
CREATE TABLE holiday (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         store_id BIGINT NOT NULL,
                         date DATE NOT NULL,

    -- 외래키(FK) 설정
                         FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE
);


-- 술집 데이터
INSERT INTO store (name, address, open_time, close_time, seat_capacity) VALUES ('낭만오지', '정문 도보 1분', '17:00:00', '00:00:00', 12);
INSERT INTO store (name, address, open_time, close_time, seat_capacity) VALUES ('캠퍼스 맥주창고', '후문 도보 3분', '17:00:00', '00:00:00', 8);
INSERT INTO store (name, address, open_time, close_time, seat_capacity) VALUES ('술이야기', '정문 도보 5분', '18:00:00', '00:00:00', 20);
INSERT INTO store (name, address, open_time, close_time, seat_capacity) VALUES ('달빛포차', '후문 도보 1분', '19:00:00', '00:00:00', 6);
INSERT INTO store (name, address, open_time, close_time, seat_capacity) VALUES ('한잔의 여유', '정문 도보 10분', '16:00:00', '00:00:00', 15);

-- 휴무일
INSERT INTO store_closed_days (store_id, day_of_week) VALUES (2, 'MONDAY');
INSERT INTO store_closed_days (store_id, day_of_week) VALUES (3, 'SUNDAY');
INSERT INTO store_closed_days (store_id, day_of_week) VALUES (4, 'MONDAY');
INSERT INTO store_closed_days (store_id, day_of_week) VALUES (4, 'TUESDAY');