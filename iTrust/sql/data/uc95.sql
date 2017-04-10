INSERT INTO patients
(MID, firstName, lastName, DateOfBirth, BloodType, Gender, obgynEligible)
VALUES
(95, 'Aaron', 'Nicholson', '1992-07-24','AB+', 'Female', true );

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (95, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO declaredhcp(PatientID,HCPID) VALUE(95, 9000000003)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;

INSERT INTO officeVisit
(patientMID, visitDate, locationID, apptTypeID, notes, weight, height, blood_pressure, household_smoking_status, patient_smoking_status, hdl, ldl, triglyceride) 
VALUES (95, '2016-07-28 11:00:00', 1, 7, 'Note', 102, 63, '104/76', 1, 4, 45, 81, 105);

SET @visit_id_2 = LAST_INSERT_ID();

INSERT INTO pregnancies
(id, birth_date, conception_year, days_pregnant, hours_labor, weight_gain, delivery_type, amount)
VALUES 
(95, '1971-01-01 00:00:00', 1970, 280, 9, 50, 'miscarriage', 1),
(95, '1972-02-01 00:00:00', 1972, 300, 9, 50, 'caesarean', 2),
(95, '1973-03-01 00:00:00', 1973, 290, 1, 41.1, 'forceps', 3),
(95, '1974-01-01 00:00:00', 1974, 290, 1, 40, 'vacuum', 4),
(95, '1991-01-04 00:00:00', 1990, 290, 1, 40, 'vaginal', 1);

INSERT INTO diagnosis
(visitId, icdCode)
VALUES
(@visit_id_2, 'O210'),
(@visit_id_2, 'E02'),
(@visit_id_2, 'X9553');

insert into allergies
(PatientID, Description, FirstFound, Code)
Values
(95, 'Codeine', '2007-06-05 20:33:33', '664662532'),
(95, 'Sulfa Drugs', '2007-06-05 20:33:33', '664662531');