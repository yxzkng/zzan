-- 테스트용 회원
INSERT INTO member (login_id, password, phone_number, role, created_at) VALUES ('testuser', 'pw1234!', '010-1234-5678', 'STUDENT', '2026-07-04 12:00:00');

-- 테스트용 술집 데이터
INSERT INTO store (name, address, open_time, close_time, seat_capacity) VALUES ('낭만오지', '정문 도보 1분', '17:00:00', '00:00:00', 12);
INSERT INTO store (name, address, open_time, close_time, seat_capacity) VALUES ('캠퍼스 맥주창고', '후문 도보 3분', '17:00:00', '00:00:00', 8);

-- 휴무일: 캠퍼스 맥주창고는 월요일 휴무
INSERT INTO store_closed_days (store_id, day_of_week) VALUES (2, 'MONDAY');

-- 테스트용 예약 (2026-07-12, 낭만오지 18시~19시에 6명 예약됨)
INSERT INTO reservation (member_id, store_id, date, start_time, end_time, headcount, status, created_at) VALUES (1, 1, '2026-07-12', '18:00:00', '19:00:00', 6, 'CONFIRMED', '2026-07-04 12:00:00');
