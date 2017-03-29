UPDATE patients SET obgynEligible=1 WHERE MID < 5;	

INSERT INTO obstetrics
(id, init_date, lmp_date, current) 
VALUES
(2, '1992-05-02 00:00:00', '1992-01-01 00:00:00', 0),
(2,'2002-05-01 00:00:00','2016-01-01 00:00:00', 1),
(2,'1996-05-03 00:00:00','1990-01-01 00:00:00', 0),
(4,'2014-05-01 00:00:00','1990-01-01 00:00:00', 0),
(6,'1990-05-01 00:00:00','1990-01-01 00:00:00', 1),
(4,'2016-05-07 00:00:00','1990-01-01 00:00:00', 1);

INSERT INTO pregnancies
(id, birth_date, conception_year, days_pregnant, hours_labor, weight_gain, delivery_type, amount)
VALUES 
(2, '1971-01-01 00:00:01', 1970, 280, 9, 50, 'miscarriage', 1),
(2, '1972-01-01 00:00:01', 1972, 300, 9, 50, 'caesarean section', 1),
(2, '1973-01-01 00:00:01', 1973, 290, 1, 40, 'vaginal delivery forceps assist', 1),
(2, '1974-01-01 00:00:01', 1974, 290, 1, 40, 'vaginal delivery vacuum assist', 1),
(2, '1991-01-01 00:00:01', 1992, 290, 1, 40, 'vaginal delivery', 1);

