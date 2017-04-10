INSERT INTO patients
(MID, firstName, lastName, DateOfBirth, BloodType, Gender, obgynEligible)
VALUES
(95, 'Aaron', 'Nicholson', '1992-07-24','AB+', 'Female', true );

INSERT INTO pregnancies
(id, birth_date, conception_year, days_pregnant, hours_labor, weight_gain, delivery_type, amount)
VALUES 
(95, '1971-01-01 00:00:00', 1970, 280, 9, 50, 'miscarriage', 1),
(95, '1972-02-01 00:00:00', 1972, 300, 9, 50, 'caesarean', 2),
(95, '1973-03-01 00:00:00', 1973, 290, 1, 41.1, 'forceps', 3),
(95, '1974-01-01 00:00:00', 1974, 290, 1, 40, 'vacuum', 4),
(95, '1991-01-04 00:00:00', 1990, 290, 1, 40, 'vaginal', 1);