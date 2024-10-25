--team
INSERT INTO team(team_stadium, team_name) VALUES
	 ('OK저축은행','안산상록수체육관'),
	 ('한국전력','수원체육관'),
	 ('현대캐피탈','천안유관순체육관'),
	 ('삼성화재','대전충무체육관'),
	 ('KB손해보험','의정부체육관'),
	 ('우리카드','서울장충체육관');
--season
INSERT INTO season(season_name, season_start_date, season_end_date) VALUES
	 ('23/24','2023-08-16','2024-07-31'),
	 ('24/25','2024-08-16','2025-07-31');

--seats
INSERT INTO seat(seat_zone, seat_section, seat_price, seat_count) VALUES
	 ('GOLD','A',30000,30),
	 ('GOLD','B',30000,30),
	 ('GOLD','C',30000,30),
	 ('BLUE-N','A',20000,30),
	 ('BLUE-N','B',20000,30),
	 ('BLUE-N','C',20000,30),
	 ('BLUE-S','A',20000,30),
	 ('BLUE-S','B',20000,30),
	 ('BLUE-S','C',20000,30),
	 ('AWAY','A',30000,30);
INSERT INTO seat(seat_zone, seat_section, seat_price, seat_count) VALUES
	 ('AWAY','B',30000,30),
	 ('AWAY','C',30000,30),
	 ('NORTH','A',10000,100),
	 ('NORTH','B',10000,100),
	 ('NORTH','C',10000,100),
	 ('WEST','A',10000,100),
	 ('WEST','B',10000,100),
	 ('WEST','C',10000,100),
	 ('SOUTH','A',10000,100),
	 ('SOUTH','B',10000,100);
INSERT INTO seat(seat_zone, seat_section, seat_price, seat_count) VALUES
	 ('SOUTH','C',10000,100),
	 ('EAST','A',10000,100),
	 ('EAST','B',10000,100),
	 ('EAST','C',10000,100);

--match
INSERT INTO match(match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id) VALUES
	 ('2023-10-14 14:00:00','HOME',3,3,0,1),
	 ('2023-10-19 19:00:00','AWAY',2,4,3,1),
	 ('2023-10-25 19:00:00','AWAY',2,6,3,1),
	 ('2023-10-28 14:00:00','HOME',3,5,2,1),
	 ('2023-11-02 19:00:00','AWAY',3,2,0,1),
	 ('2023-11-07 19:00:00','HOME',3,1,0,1),
	 ('2023-11-11 14:00:00','AWAY',1,5,3,1),
	 ('2023-11-15 19:00:00','HOME',3,4,0,1),
	 ('2023-11-18 14:00:00','HOME',1,2,3,1),
	 ('2023-11-22 19:00:00','AWAY',3,1,0,1);
INSERT INTO match(match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id) VALUES
	 ('2023-11-25 14:00:00','AWAY',3,3,0,1),
	 ('2023-11-30 19:00:00','HOME',0,6,3,1),
	 ('2023-12-07 19:00:00','AWAY',1,6,3,1),
	 ('2023-12-10 14:00:00','HOME',1,5,3,1),
	 ('2023-12-13 19:00:00','AWAY',3,2,1,1),
	 ('2023-12-17 14:00:00','HOME',3,3,0,1),
	 ('2023-12-22 19:00:00','AWAY',1,4,3,1),
	 ('2023-12-25 14:00:00','HOME',3,1,0,1),
	 ('2023-12-29 19:00:00','AWAY',0,1,3,1),
	 ('2024-01-01 14:00:00','HOME',2,2,3,1);
INSERT INTO match(match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id) VALUES
	 ('2024-01-05 19:00:00','HOME',3,6,0,1),
	 ('2024-01-09 19:00:00','AWAY',1,5,3,1),
	 ('2024-01-12 19:00:00','AWAY',3,3,2,1),
	 ('2024-01-16 19:00:00','HOME',3,4,0,1),
	 ('2024-01-30 19:00:00','HOME',2,3,3,1),
	 ('2024-02-03 14:00:00','AWAY',3,4,1,1),
	 ('2024-02-07 19:00:00','HOME',3,5,1,1),
	 ('2024-02-11 14:00:00','AWAY',0,2,3,1),
	 ('2024-02-14 19:00:00','HOME',3,1,1,1),
	 ('2024-02-17 14:00:00','AWAY',3,6,2,1);
INSERT INTO match(match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id) VALUES
	 ('2024-02-23 19:00:00','HOME',3,4,1,1),
	 ('2024-02-27 19:00:00','HOME',3,2,0,1),
	 ('2024-03-01 14:00:00','AWAY',3,3,1,1),
	 ('2024-03-06 19:00:00','HOME',0,6,3,1),
	 ('2024-03-10 19:00:00','AWAY',2,1,3,1),
	 ('2024-03-14 19:00:00','AWAY',3,5,0,1),
	 ('2024-09-19 14:00:00','HOME',3,1,1,2),
	 ('2024-09-23 19:00:00','AWAY',3,2,0,2),
	 ('2024-09-27 14:00:00','AWAY',2,3,3,2),
	 ('2024-10-01 19:00:00','HOME',2,4,3,2);
INSERT INTO match(match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id) VALUES
	 ('2024-10-05 19:00:00','HOME',3,5,0,2),
	 ('2024-10-08 19:00:00','AWAY',2,6,3,2),
	 ('2024-10-13 19:00:00','AWAY',3,5,2,2),
	 ('2024-10-16 14:00:00','HOME',2,6,3,2),
	 ('2024-10-21 19:00:00','HOME',3,2,2,2),
	 ('2024-10-24 14:00:00','AWAY',2,1,3,2),
	 ('2024-10-29 19:00:00','AWAY',0,4,0,2),
	 ('2024-11-03 19:00:00','HOME',0,3,0,2),
	 ('2024-11-08 14:00:00','AWAY',0,2,0,2),
	 ('2024-11-12 19:00:00','AWAY',0,6,0,2);
INSERT INTO match(match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id) VALUES
	 ('2024-11-15 14:00:00','HOME',0,4,0,2),
	 ('2024-11-20 19:00:00','HOME',0,1,0,2),
	 ('2024-11-25 14:00:00','AWAY',0,3,0,2),
	 ('2024-11-29 14:00:00','HOME',0,5,0,2),
	 ('2024-12-08 19:00:00','AWAY',0,4,0,2),
	 ('2024-12-12 14:00:00','AWAY',0,5,0,2),
	 ('2024-12-17 19:00:00','HOME',0,2,0,2),
	 ('2024-12-22 19:00:00','HOME',0,6,0,2),
	 ('2024-12-25 14:00:00','AWAY',0,1,0,2),
	 ('2024-12-29 19:00:00','HOME',0,3,0,2);
INSERT INTO match(match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id) VALUES
	 ('2025-01-04 19:00:00','AWAY',0,2,0,2),
	 ('2025-01-07 19:00:00','AWAY',0,6,0,2),
	 ('2025-01-11 19:00:00','HOME',0,4,0,2),
	 ('2025-01-14 19:00:00','HOME',0,5,0,2),
	 ('2025-01-18 19:00:00','AWAY',0,3,0,2),
	 ('2025-01-21 19:00:00','HOME',0,1,0,2),
	 ('2025-01-27 19:00:00','AWAY',0,4,0,2),
	 ('2025-02-02 14:00:00','HOME',0,3,0,2),
	 ('2025-02-06 19:00:00','AWAY',0,1,0,2),
	 ('2025-02-09 14:00:00','HOME',0,6,0,2);
INSERT INTO match(match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id) VALUES
	 ('2025-02-13 19:00:00','HOME',0,2,0,2),
	 ('2025-02-18 19:00:00','AWAY',0,5,0,2);

--membership
INSERT INTO membership(membership_name, membership_price, membership_season_id) VALUES
    ('23/24-bronze', 0, 1),
    ('24/25-bronze', 0, 2),
    ('23/24-silver', 100000, 1),
    ('24/25-silver', 100000, 2),
    ('23/24-gold', 300000, 1),
    ('24/25-gold', 300000, 2);

--users
INSERT INTO users(user_email, user_password, user_name, user_phone, user_address, user_membership_id, user_role, user_create_at, user_update_at)VALUES
	 ('Admin@abc.com','admin','김형민','01011112222','서울시 인사동길 12',4,'ADMIN','2023-07-20 00:00:00','2024-10-23 00:00:00'),
	 ('kimka@cbc.com','kimka3333','김가현','01022223333','서울시 강남구 역삼동',4,'USER','2023-09-10 00:00:00','2024-09-16 00:00:00'),
	 ('won@cbc.com','won4444','김예원','01033334444','경기도 고양시 덕양구',6,'USER','2023-08-24 00:00:00','2024-05-10 00:00:00'),
	 ('hyell@hew.com','hyell5555','김혜리','01044445555','강원도 속초시 설악동',4,'USER','2023-08-17 00:00:00','2024-10-12 00:00:00'),
	 ('suna@abc.com','suna6666','박선아','01055556666','서울시 도봉구 쌍문동',4,'USER','2023-09-12 00:00:00','2023-12-13 00:00:00'),
	 ('sang@dof.com','sang7777','전원상','01066667777','서울시 강남구 청담동',5,'USER','2023-10-30 00:00:00','2023-12-10 00:00:00'),
	 ('eunwoo@hew.com','eunwoo8888','차은우','01077778888','경기도 고양시 일산동구',5,'USER','2023-10-07 00:00:00','2024-03-08 00:00:00'),
	 ('knife@hew.com','knife9999','박보검','01088889999','인천시 부평구 십정동',4,'USER','2023-10-03 00:00:00','2024-01-23 00:00:00'),
	 ('jojo@efr.com','jojo1111','조규성','01099991111','경기도 고양시 일산동구',6,'USER','2023-09-30 00:00:00','2024-09-11 00:00:00'),
	 ('jang@dof.com','jang3333','장원영','01011113333','강원도 속초시 설악동',6,'USER','2024-08-27 00:00:00','2024-10-11 00:00:00');
INSERT INTO users(user_email, user_password, user_name, user_phone, user_address, user_membership_id, user_role, user_create_at, user_update_at)VALUES
	 ('yujin@huj.com','yujin5555','안유진','01033335555','인천시 계양구 장기동',4,'USER','2023-09-01 00:00:00','2023-09-01 00:00:00'),
	 ('jenny@fghk.net','jenny6666','김제니','01033336666','서울시 양천구 목동',5,'USER','2024-09-04 00:00:00','2024-10-08 00:00:00'),
	 ('song@sbhvj.net','song7777','송강','01033337777','서울시 도봉구 쌍문동',4,'USER','2024-10-17 00:00:00','2024-10-17 00:00:00'),
	 ('dragon@sfbhg.net','dragon8888','기성용','01033338888','제주도 서귀포시 중문동',6,'USER','2024-10-11 00:00:00','2024-10-22 00:00:00');

--payment
INSERT INTO payment(payment_amount, payment_user_id, payment_create_at) VALUES
	 (400000,3,'2024-08-01 10:46:00'),
	 (100000,6,'2024-08-01 10:51:00'),
	 (400000,7,'2024-08-01 11:08:00'),
	 (300000,9,'2024-08-01 11:20:00'),
	 (300000,10,'2024-08-02 09:16:00'),
	 (400000,12,'2024-08-02 17:38:00'),
	 (300000,14,'2024-08-03 12:20:00'),
	 (100000,2,'2024-10-23 14:40:00'),
	 (100000,8,'2024-10-24 20:21:00'),
	 (100000,13,'2024-10-24 21:30:00');

--membership_sales
INSERT INTO membership_sales(membership_sales_user_id, membership_sales_membership_id, membership_sales_create_at) VALUES
	 (3,6,'2024-08-01 11:05:00'),
	 (6,5,'2024-08-01 11:11:00'),
	 (7,6,'2024-08-01 11:12:00'),
	 (9,6,'2024-08-01 11:26:00'),
	 (10,6,'2024-08-02 09:27:00'),
	 (12,5,'2024-08-02 17:41:00'),
	 (14,6,'2024-08-03 12:33:00');

--ticket
INSERT INTO ticket(ticket_number, ticket_match_id, ticket_user_id, ticket_paid_price, ticket_create_at) VALUES
	 ('T2024101612244523',45,3,42000,'2024-10-16 12:24:00'),
	 ('T2024101614094516',45,6,27000,'2024-10-16 14:09:00'),
	 ('T2024101710444537',45,7,42000,'2024-10-17 10:44:00'),
	 ('T2024101716504529',45,9,42000,'2024-10-17 16:50:00'),
	 ('T20241019111246410',46,10,28000,'2024-10-19 11:12:00'),
	 ('T20241020184646212',46,12,18000,'2024-10-20 18:46:00'),
	 ('T20241023130447314',47,14,21000,'2024-10-23 13:04:00'),
	 ('T2024102314554712',47,2,20000,'2024-10-23 14:55:00'),
	 ('T2024102420234728',47,8,20000,'2024-10-24 20:23:00'),
	 ('T20241024213647213',47,13,60000,'2024-10-24 21:36:00');

--ticket_detail
INSERT INTO ticket_detail(ticket_detail_seat_id, ticket_detail_amount, ticket_detail_ticket_id) VALUES
	 (2,2,1),
	 (11,1,2),
	 (5,3,3),
	 (3,2,4),
	 (14,4,5),
	 (16,2,6),
	 (20,3,7),
	 (16,1,8),
	 (23,2,9),
	 (11,2,10);