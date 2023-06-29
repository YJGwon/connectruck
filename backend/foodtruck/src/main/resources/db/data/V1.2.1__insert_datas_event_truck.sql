INSERT INTO event
    (name, location)
VALUES
    ('서울FC 경기', '서울 마포구 성산동 509-7'),
    ('여의도 밤도깨비 야시장', '서울 영등포구 여의동 여의동로 330')
;

INSERT INTO schedule
    (event_date, open_hour, close_hour, event_id)
VALUES
    (NOW(), '12:00', '17:00', 1),
    ('2023-07-08', '18:00', '23:00', 2),
    ('2023-07-09', '18:00', '23:00', 2)
;

INSERT INTO truck
    (name, car_number)
VALUES
    ('서울FC 경기장 트럭1', '00가0001'),
    ('서울FC 경기장 트럭2', '00가0002'),
    ('서울FC 경기장 트럭3', '00가0003'),
    ('서울FC 경기장 트럭4', '00가0004'),
    ('서울FC 경기장 트럭5', '00가0005'),
    ('서울FC 경기장 트럭6', '00가0006'),
    ('서울FC 경기장 트럭7', '00가0007'),
    ('서울FC 경기장 트럭8', '00가0008'),
    ('서울FC 경기장 트럭9', '00가0009'),
    ('서울FC 경기장 트럭10', '00가0010'),
    ('밤도깨비 야시장 트럭1', '00가0011'),
    ('밤도깨비 야시장 트럭2', '00가0012'),
    ('밤도깨비 야시장 트럭3', '00가0013'),
    ('밤도깨비 야시장 트럭4', '00가0014'),
    ('밤도깨비 야시장 트럭5', '00가0015'),
    ('밤도깨비 야시장 트럭6', '00가0016'),
    ('밤도깨비 야시장 트럭7', '00가0017'),
    ('밤도깨비 야시장 트럭8', '00가0018'),
    ('밤도깨비 야시장 트럭9', '00가0019'),
    ('밤도깨비 야시장 트럭10', '00가0020'),
    ('밤도깨비 야시장 트럭11', '00가0021'),
    ('밤도깨비 야시장 트럭12', '00가0022'),
    ('밤도깨비 야시장 트럭13', '00가0023'),
    ('밤도깨비 야시장 트럭14', '00가0024'),
    ('밤도깨비 야시장 트럭15', '00가0025')
;

INSERT INTO event_truck
    (event_id, truck_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7),
    (1, 8),
    (1, 9),
    (1, 10),
    (2, 11),
    (2, 12),
    (2, 13),
    (2, 14),
    (2, 15),
    (2, 16),
    (2, 17),
    (2, 18),
    (2, 19),
    (2, 20),
    (2, 21),
    (2, 22),
    (2, 23),
    (2, 24),
    (2, 25)
;
