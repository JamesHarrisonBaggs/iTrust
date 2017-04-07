UPDATE patients SET obgynEligible=1 WHERE MID < 5;	

INSERT INTO obstetrics_inits
(id, init_date, lmp_date) 
VALUES
(2, '1992-05-02 00:00:00', '1992-01-01 00:00:00'),
(2,'2016-01-01 00:00:00','2016-01-01 00:00:00'),
(2,'1996-05-03 00:00:00','1990-01-01 00:00:00'),
(4,'2014-05-01 00:00:00','1990-01-01 00:00:00'),
(6,'1990-05-01 00:00:00','1990-01-01 00:00:00'),
(4,'2016-05-07 00:00:00','1990-01-01 00:00:00');

INSERT INTO pregnancies
(id, birth_date, conception_year, days_pregnant, hours_labor, weight_gain, delivery_type, amount)
VALUES 
(2, '1971-01-01 00:00:00', 1970, 280, 9, 50, 'miscarriage', 1),
(2, '1972-01-01 00:00:00', 1972, 300, 9, 50, 'caesarean', 1),
(2, '1973-01-01 00:00:00', 1973, 290, 1, 41.1, 'forceps', 1),
(2, '1974-01-01 00:00:00', 1974, 290, 1, 40, 'vacuum', 1),
(2, '1991-01-01 00:00:00', 1990, 290, 1, 40, 'vaginal', 1);