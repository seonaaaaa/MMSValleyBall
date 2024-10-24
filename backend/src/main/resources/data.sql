INSERT INTO team(team_stadium, team_name) VALUES('테스트팀', '테스트구장');
INSERT INTO season(season_name, season_start_date, season_end_date) VALUES('yy/yy', '2023-11-01', '2024-10-31');
INSERT INTO seat(seat_zone, seat_section, seat_price, seat_count) VALUES('TS', 'A', 1000, 1);
INSERT INTO match(match_date, match_stadium, match_set_score, match_opponent_team_id, match_opponent_team_set_score, match_season_id)
VALUES('2024-10-30 19:00', 'HOME', 0, 1, 0, 1);
INSERT INTO membership(membership_name, membership_price, membership_season_id) VALUES('테스트등급', 100000, 1);
INSERT INTO users(user_email, user_password, user_name, user_membership_id, user_role, user_create_at)
VALUES('test@test.ts', 'test', '테스트', 1, 'USER', '2000-01-01 00:00');
INSERT INTO payment(payment_amount, payment_user_id, payment_create_at) VALUES(10000, 1, '2000-01-01 00:00');
INSERT INTO membership_sales(membership_sales_user_id, membership_sales_membership_id, membership_sales_create_at) VALUES(1, 1, '2000-01-01 00:00');
INSERT INTO ticket(ticket_number, ticket_match_id, ticket_user_id, ticket_create_at) VALUES('T20241023175511', 1, 1, '2000-01-01 00:00');
INSERT INTO ticket_detail(ticket_detail_seat_id, ticket_detail_amount, ticket_detail_ticket_id) VALUES(1, 1, 1);