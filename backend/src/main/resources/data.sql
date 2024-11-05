--team
INSERT INTO team(team_name, team_stadium) VALUES
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
	 ('BLUE','A',20000,30),
	 ('BLUE','B',20000,30),
	 ('BLUE','C',20000,30),
	 ('BLUE','D',20000,30),
	 ('BLUE','E',20000,30),
	 ('BLUE','F',20000,30),
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
INSERT INTO match (match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id, match_round_id) VALUES
    ('2023-10-14 14:00:00','HOME',3,3,0,1,1),
    ('2023-10-19 19:00:00','AWAY',2,4,3,1,1),
    ('2023-10-25 19:00:00','AWAY',2,6,3,1,1),
    ('2023-10-28 14:00:00','HOME',3,5,2,1,1),
    ('2023-11-02 19:00:00','AWAY',3,2,0,1,1),
    ('2023-11-07 19:00:00','HOME',3,1,0,1,1),
    ('2023-11-11 14:00:00','AWAY',1,5,3,1,2),
    ('2023-11-15 19:00:00','HOME',3,4,0,1,2),
    ('2023-11-18 14:00:00','HOME',1,2,3,1,2),
    ('2023-11-22 19:00:00','AWAY',3,1,0,1,2);
INSERT INTO match (match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id, match_round_id) VALUES
    ('2023-11-25 14:00:00','AWAY',3,3,0,1,2),
    ('2023-11-30 19:00:00','HOME',0,6,3,1,2),
    ('2023-12-07 19:00:00','AWAY',1,6,3,1,3),
    ('2023-12-10 14:00:00','HOME',1,5,3,1,3),
    ('2023-12-13 19:00:00','AWAY',3,2,1,1,3),
    ('2023-12-17 14:00:00','HOME',3,3,0,1,3),
    ('2023-12-22 19:00:00','AWAY',1,4,3,1,3),
    ('2023-12-25 14:00:00','HOME',3,1,0,1,3),
    ('2023-12-29 19:00:00','AWAY',0,1,3,1,4),
    ('2024-01-01 14:00:00','HOME',2,2,3,1,4);
INSERT INTO match (match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id, match_round_id) VALUES
    ('2024-01-05 19:00:00','HOME',3,6,0,1,4),
    ('2024-01-09 19:00:00','AWAY',1,5,3,1,4),
    ('2024-01-12 19:00:00','AWAY',3,3,2,1,4),
    ('2024-01-16 19:00:00','HOME',3,4,0,1,4),
    ('2024-01-30 19:00:00','HOME',2,3,3,1,5),
    ('2024-02-03 14:00:00','AWAY',3,4,1,1,5),
    ('2024-02-07 19:00:00','HOME',3,5,1,1,5),
    ('2024-02-11 14:00:00','AWAY',0,2,3,1,5),
    ('2024-02-14 19:00:00','HOME',3,1,1,1,5),
    ('2024-02-17 14:00:00','AWAY',3,6,2,1,5);
INSERT INTO match (match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id, match_round_id) VALUES
    ('2024-02-23 19:00:00','HOME',3,4,1,1,6),
    ('2024-02-27 19:00:00','HOME',3,2,0,1,6),
    ('2024-03-01 14:00:00','AWAY',3,3,1,1,6),
    ('2024-03-06 19:00:00','HOME',0,6,3,1,6),
    ('2024-03-10 19:00:00','AWAY',2,1,3,1,6),
    ('2024-03-14 19:00:00','AWAY',3,5,0,1,6),
    ('2024-09-19 14:00:00','HOME',3,1,1,2,1),
    ('2024-09-23 19:00:00','AWAY',3,2,0,2,1),
    ('2024-09-27 14:00:00','AWAY',2,3,3,2,1),
    ('2024-10-01 19:00:00','HOME',2,4,3,2,1);
INSERT INTO match (match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id, match_round_id) VALUES
    ('2024-10-05 19:00:00','HOME',3,5,0,2,1),
    ('2024-10-08 19:00:00','AWAY',2,6,3,2,1),
    ('2024-10-13 19:00:00','AWAY',3,5,2,2,2),
    ('2024-10-16 14:00:00','HOME',2,6,3,2,2),
    ('2024-10-21 19:00:00','HOME',3,2,2,2,2),
    ('2024-10-24 14:00:00','AWAY',2,1,3,2,2),
    ('2024-10-29 19:00:00','AWAY',3,4,1,2,2),
    ('2024-11-03 19:00:00','HOME',3,3,1,2,2),
    ('2024-11-08 14:00:00','AWAY',0,2,0,2,3),
    ('2024-11-12 19:00:00','AWAY',0,6,0,2,3);
INSERT INTO match (match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id, match_round_id) VALUES
    ('2024-11-15 14:00:00','HOME',0,4,0,2,3),
    ('2024-11-20 19:00:00','HOME',0,1,0,2,3),
    ('2024-11-25 14:00:00','AWAY',0,3,0,2,3),
    ('2024-11-29 14:00:00','HOME',0,5,0,2,3),
    ('2024-12-08 19:00:00','AWAY',0,4,0,2,4),
    ('2024-12-12 14:00:00','AWAY',0,5,0,2,4),
    ('2024-12-17 19:00:00','HOME',0,2,0,2,4),
    ('2024-12-22 19:00:00','HOME',0,6,0,2,4),
    ('2024-12-25 14:00:00','AWAY',0,1,0,2,4),
    ('2024-12-29 19:00:00','HOME',0,3,0,2,4);
INSERT INTO match (match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id, match_round_id) VALUES
    ('2025-01-04 19:00:00','AWAY',0,2,0,2,5),
    ('2025-01-07 19:00:00','AWAY',0,6,0,2,5),
    ('2025-01-11 19:00:00','HOME',0,4,0,2,5),
    ('2025-01-14 19:00:00','HOME',0,5,0,2,5),
    ('2025-01-18 19:00:00','AWAY',0,3,0,2,5),
    ('2025-01-21 19:00:00','HOME',0,1,0,2,5),
    ('2025-01-27 19:00:00','AWAY',0,4,0,2,6),
    ('2025-02-02 14:00:00','HOME',0,3,0,2,6),
    ('2025-02-06 19:00:00','AWAY',0,1,0,2,6),
    ('2025-02-09 14:00:00','HOME',0,6,0,2,6);
INSERT INTO match (match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id, match_round_id) VALUES
    ('2025-02-13 19:00:00','HOME',0,2,0,2,6),
    ('2025-02-18 19:00:00','AWAY',0,5,0,2,6);

--membership
INSERT INTO membership(membership_price, membership_season_id,  membership_name) VALUES
	 (0,1,'23/24-bronze'),
	 (100000,1,'23/24-silver'),
	 (300000,1,'23/24-gold'),
	 (0,2,'24/25-bronze'),
	 (100000,2,'24/25-silver'),
	 (300000,2,'24/25-gold');

--users
INSERT INTO users(user_email, user_password, user_name, user_phone, user_address, user_membership_id, user_role, user_create_at, user_update_at)VALUES
	 ('Admin@abc.com','admin','김형민','01011112222','서울시 인사동길 12',4,'ADMIN','2023-07-20 00:00:00','2024-10-23 00:00:00'),
	 ('ggg0909134@naver.com','kimka3333','김가현','01022223333','서울시 강남구 역삼동',4,'USER','2023-09-10 00:00:00','2024-09-16 00:00:00'),
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
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:34:10.106351', '4','010-2936-2762', '강민희', 'gang0917@gmail.com', '전남 순천시 조례동 12', '$2a$10$567KfWWgKR/gRMM5NOD/iuC6Z7g9eO.ZKwkRClLDuzylbJD4M49kC', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:35:00.894228', '4','010-7492-0643', '구정모', 'gu999@gmail.com', '서울 강남구 삼성동 25', '$2a$10$/OqnjveQInUTPL14s8gE3e1ByIKZvXaNRuL0YpeLr0Xyl185d.kh2', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:35:45.039522', '4','010-0083-6181', '송형준', 'song11@naver.com', '경남 통영시 한산면 가왕길 22', '$2a$10$Cz2ScP6J0Cq8lo0P5BeHHeioAdPxDztJOydiaTkCCWj9qPOWZ6PfK', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:36:50.815504', '4','010-8363-8692', '안성민', 'ahnn03@naver.com', '서울 송파구 오금동 70', '$2a$10$64bArRdqvl5hRlPoAkdFDewMgjNPuMfAoseU6AMmwctqjpBv1sH7S', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:37:30.361907', '4','010-2820-0181', '함원진', 'ham02@naver.com', '경기 고양시 일산동구 식사동 51-1', '$2a$10$gdmpMLPtAiGMBwWt5.vGx.dcVP31Mi4oaM.kr5jFEgaL3GYYKjyPW', 'ADMIN', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:38:06.20163', '4','010-7890-3333', '최지우', 'user1@naver.com', '서울 강북구 수유동 45-1', '$2a$10$aGx3F5KLdH2gE3u7jexO7eFXzZtEGgyPeaPOyd4A.jkKmK8G5/UT2', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:39:11.264977', '4','010-7890-2345', '이동욱', 'user2@gmail.com', '부산 북구 구포동 산 36-23', '$2a$10$ja0DOm8iOIqowGnu0fvpcucvhIM9jKh.4LwVDGzm0ZToarH54pMRy', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:40:10.661695', '4','010-7254-3234', '강민지', 'user3@naver.com', '경기 수원시 영통구 광교로 32', '$2a$10$3OAPwiG2G.DFwvc4cs/jYena0E7PZiHlPJUws2XhKG.xU6axCwvNW', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:40:53.17973', '4','010-7824-3012', '최수정', 'user4@naver.com', '서울 송파구 잠실동 27', '$2a$10$n8LPAv68OZ0BBXDhyVgcU.VNuuDv.BdVb9zd.ad1ZM4juSJ1B14/y', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:41:33.737516', '4','010-4633-8620', '김태희', 'user5@dof.com', '서울 강남구 삼성동 5', '$2a$10$CPTgIfEAIuH1uhS/rrxTl.lX12VGnL9JYucx4g3MVT/k5AJ2lthBm', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:42:12.873967', '4','010-3592-9521', '이민호', 'user6@abc.com', '전북특별자치도 전주시 완산구 간납대1길 14', '$2a$10$JI8m6q6kYZerC//ipfunTewASismQGhhTWl.FhAiaK1EkXn53dCz2', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:43:13.34586', '4','010-0123-6875', '박신혜', 'user7@efr.com', '충남 천안시 동남구 가마골1길 5', '$2a$10$1BUOGy51kKoGntqZqtNxvugEx8AmDLEveJO0VNB7HkXGM3Vb11Qpe', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:43:50.40614', '4','010-9844-6547', '이정재', 'user8@gmail.com', '서울 마포구 독막로3길 13', '$2a$10$5Qao54Fu1CTmhpgHk//e8e535rq2fUFT87slAQv1jD5OF48pXG2Hu', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:44:34.035834', '4','010-2134-8656', '김소현', 'user9@naver.com', '인천 연수구 송도동 78', '$2a$10$TPA8P4fo6YkGB.k55i7QDeQ/iqm9k.KSfKpldj8wYC2l7Q6IGM.ke', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:45:11.500029', '4','010-6354-8413', '한소희', 'user11@abc.com', '경기 의정부시 호원동 산 44-1', '$2a$10$SfvIBnag2Qy2SxPZKBnchOcMzh4A8TWawofWi6wrWr5zi1GHZGh92', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:45:47.001608', '4','010-5646-8854', '김유정', 'user12@efr.com', '서울 서대문구 연희동 산 22-4', '$2a$10$LR/dp.AjMuNniysSqRlGAeLSMaRW3VePIBW.Jw4UiUP8ywW4eEAhO', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:46:33.301063', '4','010-1832-6873', '박보영', 'user10@dof.com', '대구 달서구 상원로 3', '$2a$10$5Lmbd7M.SHbQBkyIhxfXPOHYQeIk9vWfTQZDvEL2D5JrktU05jCKu', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:47:07.457109', '4','010-6841-4586', '정해인', 'user13@gmail.com', '경기 파주시 문산읍 당동리 50-4', '$2a$10$Z5kufzSkamMVUpkn6KDgZOHUgf3zgZ2BcuXX513HEdvtPxOAFUdp6', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:47:46.488469', '4','010-8641-6487', '송중기', 'user14@naver.com', '전남 목포시 남악로 22-5', '$2a$10$OLwh43vsALxGYaC01duYlus/8gK4h4orKWOpFb1hpJKemPEHqimQy', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:48:25.1312', '4','010-7527-4536', '최진영', 'user15@dof.com', '충북 청주시 상당구 가장로332번길 213', '$2a$10$MpgRukbdcphoGVH2xGAfneg88vc.GU.igUpzAokXSFAC0gTLDb2Ue', 'USER', 'ACTIVE');
INSERT INTO users (user_create_at, user_membership_id, user_phone, user_name, user_email, user_address, user_password, user_role, user_status) VALUES ('2024-11-04 10:49:06.80922', '4','010-7578-4723', '김지현', 'user16@abc.com', '서울 강동구 천호동 53-1', '$2a$10$9IYDOfx4xfX.8jFwD1TH9ezk1L7HlBQp1thOmbt5pDr1RbppBcdRu', 'USER', 'ACTIVE');

----payment
INSERT INTO payment (payment_amount,payment_create_at,payment_user_id,payment_status) VALUES
	 (400000,'2023-08-01 10:46:00',3,'COMPLETED'),
	 (400000,'2023-08-01 10:51:00',6,'COMPLETED'),
	 (600000,'2023-08-01 11:08:00',7,'COMPLETED'),
	 (600000,'2023-08-01 11:20:00',9,'COMPLETED'),
	 (600000,'2023-08-02 09:16:00',10,'COMPLETED'),
	 (400000,'2023-08-02 17:38:00',12,'COMPLETED'),
	 (350000,'2023-08-03 12:20:00',14,'COMPLETED'),
	 (400000,'2023-08-03 12:40:00',2,'COMPLETED'),
	 (200000,'2023-08-03 20:21:00',8,'COMPLETED'),
	 (300000,'2023-08-04 21:30:00',11,'COMPLETED'),
	 (100000,'2023-10-04 21:30:00',20,'COMPLETED');
INSERT INTO payment (payment_amount,payment_create_at,payment_user_id,payment_status) VALUES
	 (100000,'2023-10-15 21:30:00',13,'COMPLETED'),
	 (100000,'2023-10-16 21:30:00',15,'COMPLETED'),
	 (100000,'2023-10-17 21:30:00',16,'COMPLETED'),
	 (100000,'2023-10-18 21:30:00',17,'COMPLETED'),
	 (100000,'2023-10-23 21:30:00',18,'COMPLETED'),
	 (100000,'2023-10-24 17:30:00',19,'COMPLETED'),
	 (100000,'2023-10-24 21:30:00',20,'COMPLETED'),
	 (100000,'2023-11-04 11:30:00',21,'COMPLETED'),
	 (100000,'2023-12-01 10:00:00',22,'COMPLETED'),
	 (100000,'2024-01-01 10:00:00',23,'COMPLETED');
INSERT INTO payment (payment_amount,payment_create_at,payment_user_id,payment_status) VALUES
	 (100000,'2024-02-01 10:00:00',24,'COMPLETED'),
	 (200000,'2024-07-01 11:11:00',6,'COMPLETED'),
	 (200000,'2024-07-11 11:11:00',7,'COMPLETED'),
	 (400000,'2024-07-21 11:11:00',8,'COMPLETED'),
	 (200000,'2024-07-21 13:11:00',10,'COMPLETED'),
	 (150000,'2024-07-22 11:11:00',11,'COMPLETED'),
	 (400000,'2024-07-23 11:11:00',12,'COMPLETED'),
	 (200000,'2024-07-26 11:11:00',13,'COMPLETED'),
	 (140000,'2024-07-27 11:11:00',15,'COMPLETED'),
	 (120000,'2024-07-28 11:11:00',17,'COMPLETED'),
	 (150000,'2024-07-29 11:11:00',18,'COMPLETED'),
	 (400000,'2024-07-30 11:11:00',19,'COMPLETED'),
	 (400000,'2024-07-31 11:11:00',22,'COMPLETED'),
	 (200000,'2024-08-01 11:11:00',23,'COMPLETED'),
	 (400000,'2024-08-01 11:21:00',25,'COMPLETED'),
	 (150000,'2024-08-01 11:31:00',28,'COMPLETED'),
	 (200000,'2024-08-01 15:11:00',29,'COMPLETED'),
	 (400000,'2024-08-02 11:11:00',31,'COMPLETED'),
	 (150000,'2024-08-02 13:11:00',32,'COMPLETED');

--membership_sales
INSERT INTO membership_sales (membership_sales_create_at,membership_sales_membership_id,membership_sales_user_id,membership_sales_status) VALUES
	 ('2023-08-01 11:05:00',2,3,'PURCHASE'),
	 ('2023-08-01 11:11:00',3,6,'PURCHASE'),
	 ('2023-08-01 11:12:00',3,7,'PURCHASE'),
	 ('2023-08-01 11:26:00',2,9,'PURCHASE'),
	 ('2023-08-02 09:27:00',2,10,'PURCHASE'),
	 ('2023-08-02 17:41:00',3,12,'PURCHASE'),
	 ('2023-08-03 12:33:00',2,14,'PURCHASE'),
	 ('2023-08-03 13:33:00',2,11,'PURCHASE'),
	 ('2023-08-03 13:43:00',3,2,'PURCHASE'),
	 ('2023-08-03 14:33:00',2,8,'PURCHASE');
INSERT INTO membership_sales (membership_sales_create_at,membership_sales_membership_id,membership_sales_user_id,membership_sales_status) VALUES
	 ('2024-08-01 11:11:00',5,6,'PURCHASE'),
	 ('2024-08-01 11:12:00',5,7,'PURCHASE'),
	 ('2024-08-01 11:26:00',6,8,'PURCHASE'),
	 ('2024-08-02 09:27:00',5,10,'PURCHASE'),
	 ('2024-08-02 17:41:00',5,11,'PURCHASE'),
	 ('2024-08-03 12:33:00',6,12,'PURCHASE'),
	 ('2024-08-03 12:35:00',5,13,'PURCHASE'),
	 ('2024-08-03 12:44:00',5,15,'PURCHASE'),
	 ('2024-08-03 12:59:00',5,17,'PURCHASE'),
	 ('2024-08-03 16:33:00',5,18,'PURCHASE');
INSERT INTO membership_sales (membership_sales_create_at,membership_sales_membership_id,membership_sales_user_id,membership_sales_status) VALUES
	 ('2024-08-03 19:33:00',6,19,'PURCHASE'),
	 ('2024-08-03 21:33:00',6,22,'PURCHASE'),
	 ('2024-08-04 10:33:00',5,23,'PURCHASE'),
	 ('2024-08-04 12:33:00',6,25,'PURCHASE'),
	 ('2024-08-04 17:33:00',5,28,'PURCHASE'),
	 ('2024-08-05 01:33:00',5,29,'PURCHASE'),
	 ('2024-08-05 10:33:00',6,31,'PURCHASE'),
	 ('2024-08-06 16:33:00',5,32,'PURCHASE');

--ticket
INSERT INTO ticket (ticket_paid_price,ticket_create_at,ticket_match_id,ticket_user_id,ticket_number,ticket_status) VALUES
	 (40000,'2023-10-10 11:00:00',1,4,'T2023101011000001404','BOOKED'),
	 (40000,'2023-10-11 11:00:00',1,5,'T2023101111000001405','BOOKED'),
	 (40000,'2023-10-12 11:00:00',1,12,'T2023101211000001412','BOOKED'),
	 (40000,'2023-10-12 11:00:00',1,6,'T2023101211000001406','BOOKED'),
	 (40000,'2023-10-30 12:00:00',5,12,'T2023103012000005412','BOOKED'),
	 (40000,'2023-10-30 14:00:00',5,19,'T2023103014000005419','BOOKED'),
	 (40000,'2023-10-31 12:00:00',5,16,'T2023103112000005416','BOOKED'),
	 (40000,'2023-10-31 20:00:00',5,7,'T2023103120000005407','BOOKED'),
     (72000,'2023-11-01 10:00:00',5,14,'T2023110110000005414','BOOKED'),
	 (40000,'2023-11-18 12:00:00',10,17,'T2023111812000010417','BOOKED'),
	 (40000,'2023-11-18 16:00:00',10,6,'T2023111816000010406','BOOKED'),
	 (72000,'2023-11-18 11:00:00',10,13,'T2023111811000010413','BOOKED'),
     (40000,'2023-11-19 11:00:00',10,3,'T2023111911000010403','BOOKED'),
	 (40000,'2023-12-09 11:00:00',15,9,'T2023120911000015409','BOOKED'),
	 (40000,'2023-12-09 12:00:00',15,2,'T2023120912000015402','BOOKED'),
	 (40000,'2023-12-10 11:00:00',15,11,'T2023121011000015411','BOOKED'),
	 (40000,'2023-12-10 14:00:00',15,15,'T2023121014000015415','BOOKED'),
	 (40000,'2023-12-29 11:00:00',20,6,'T2023122911000020406','BOOKED'),
     (72000,'2023-12-29 12:00:00',20,8,'T2023122912000020408','BOOKED'),
     (56000,'2023-12-29 13:00:00',20,2,'T2023122913000020402','BOOKED'),
	 (40000,'2023-12-29 14:00:00',20,4,'T2023122914000020404','BOOKED'),
	 (40000,'2023-12-30 11:00:00',20,3,'T2023123011000020403','BOOKED'),
	 (40000,'2024-01-26 11:00:00',25,16,'T2024012611000025416','BOOKED'),
	 (40000,'2024-01-26 12:00:00',25,17,'T2024012612000025417','BOOKED'),
	 (40000,'2024-01-27 11:00:00',25,20,'T2024012711000025420','BOOKED'),
	 (40000,'2024-01-28 11:00:00',25,23,'T2024012811000025423','BOOKED'),
	 (40000,'2024-02-13 11:00:00',30,10,'T2024021311000030410','BOOKED'),
	 (40000,'2024-02-13 13:00:00',30,14,'T2024021313000030414','BOOKED'),
     (72000,'2024-02-13 21:00:00',30,18,'T2024021321000030418','BOOKED'),
     (80000,'2024-02-14 11:00:00',30,22,'T2024021411000030422','BOOKED'),
	 (40000,'2024-03-10 11:00:00',36,7,'T2024031011000036407','BOOKED'),
	 (40000,'2024-03-10 12:00:00',36,11,'T2024031012000036411','BOOKED'),
	 (40000,'2024-03-10 13:00:00',36,15,'T2024031013000036415','BOOKED'),
	 (40000,'2024-03-11 11:00:00',36,19,'T2024031111000036419','BOOKED'),
	 (40000,'2024-03-11 21:00:00',36,24,'T2024031121000036424','BOOKED');
INSERT INTO ticket (ticket_paid_price,ticket_create_at,ticket_match_id,ticket_user_id,ticket_number,ticket_status) VALUES
	 (40000,'2024-09-14 11:00:00',37,2,'T2024091411000037402','BOOKED'),
	 (40000,'2024-09-14 12:00:00',37,5,'T2024091412000037405','BOOKED'),
	 (56000,'2024-09-14 13:00:00',37,19,'T2024091413000037419','BOOKED'),
	 (56000,'2024-09-15 11:00:00',37,31,'T2024091511000037431','BOOKED'),
	 (56000,'2024-09-22 11:00:00',39,22,'T2024092211000039422','BOOKED'),
	 (40000,'2024-09-22 12:00:00',39,17,'T2024092212000039417','BOOKED'),
	 (28000,'2024-09-23 11:00:00',39,8,'T2024092311000039408','BOOKED'),
	 (36000,'2024-09-23 21:00:00',39,23,'T2024092321000039423','BOOKED'),
	 (80000,'2024-10-03 11:00:00',42,26,'T2024100311000042426','BOOKED'),
	 (56000,'2024-10-03 12:00:00',42,8,'T2024100312000042408','BOOKED'),
	 (28000,'2024-10-04 10:00:00',42,29,'T2024100410000042429','BOOKED'),
	 (40000,'2024-10-04 11:00:00',42,4,'T2024100411000042404','BOOKED'),
	 (40000,'2024-10-15 11:00:00',45,33,'T2024101511000045433','BOOKED'),
	 (56000,'2024-10-16 11:00:00',45,12,'T2024101611000045412','BOOKED'),
	 (40000,'2024-10-16 11:30:00',45,21,'T2024101611300045421','BOOKED'),
	 (36000,'2024-10-17 11:00:00',45,10,'T2024101711000045410','BOOKED'),
	 (40000,'2024-10-24 11:00:00',47,20,'T2024102411000047420','BOOKED'),
	 (40000,'2024-10-24 12:00:00',47,9,'T2024102412000047409','BOOKED'),
	 (72000,'2024-10-24 17:00:00',47,15,'T2024102417000047415','BOOKED'),
     (72000,'2024-10-25 11:00:00',47,13,'T2024102511000047413','BOOKED'),
	 (40000,'2024-10-28 11:00:00',48,27,'T2024102811000048427','BOOKED'),
	 (28000,'2024-10-29 11:00:00',48,22,'T2024102911000048422','BOOKED'),
	 (40000,'2024-10-29 11:30:00',48,16,'T2024102911300048416','BOOKED'),
	 (40000,'2024-10-30 11:00:00',48,30,'T2024103011000048430','BOOKED'),
     (84000,'2024-11-03 11:10:00',49,31,'T2024110311100049431','BOOKED');

--ticket_detail
INSERT INTO ticket_detail (ticket_detail_amount,ticket_detail_seat_id,ticket_detail_ticket_id) VALUES
	 (4,13,1),
	 (4,14,2),
	 (4,15,3),
	 (4,16,4),
	 (4,17,5),
	 (4,18,6),
	 (4,19,7),
	 (4,20,8),
	 (4,4,9),
	 (4,5,10),
	 (4,13,11),
	 (4,14,12),
	 (4,15,13),
	 (4,16,14),
	 (4,17,15),
	 (4,18,16),
	 (4,19,17),
	 (4,20,18),
	 (4,6,19),
	 (4,7,20),
	 (4,13,21),
	 (4,14,22),
	 (4,15,23),
	 (4,16,24),
	 (4,17,25),
	 (4,18,26),
	 (4,19,27),
	 (4,20,28),
	 (4,4,29),
	 (4,8,30),
	 (4,13,31),
	 (4,14,32),
	 (4,15,33),
	 (4,16,34),
	 (4,17,35),
	 (4,21,36),
	 (4,20,37),
	 (4,5,38),
	 (4,4,39),
	 (4,7,40),
	 (4,19,41),
	 (4,18,42),
	 (4,17,43),
	 (4,6,44),
	 (4,8,45),
	 (4,14,46),
	 (4,12,47),
	 (4,13,48),
	 (4,4,49),
	 (4,21,50),
	 (4,19,51),
	 (4,13,52),
	 (4,16,53),
	 (4,5,54),
	 (4,6,55),
	 (4,18,56),
	 (4,17,57),
	 (4,8,58),
	 (4,15,59),
	 (4,2,60);
