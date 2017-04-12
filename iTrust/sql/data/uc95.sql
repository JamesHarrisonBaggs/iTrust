INSERT INTO patients
(MID, 
firstName,
lastName, 
email,
address1,
address2,
city,
state,
zip,
phone,
eName,
ePhone,
iCName,
iCAddress1,
iCAddress2,
iCCity, 
ICState,
iCZip,
iCPhone,
iCID,
DateOfBirth,
DateOfDeath,
CauseOfDeath,
MotherMID,
FatherMID,
BloodType,
Ethnicity,
Gender,
obgynEligible,
TopicalNotes
)
VALUES (
85,
'Aaron',
'Nicholson',
'Aaron.Nicholson@gmail.com',
'344 Bob Street',
'',
'Raleigh',
'NC',
'27607',
'555-555-5555',
'Mr Emergency',
'555-555-5551',
'IC',
'Street1',
'Street2',
'City',
'PA',
'19003-2715',
'555-555-5555',
'1',
'1992-07-24',
'2005-03-10',
'250.10',
1,
0,
'AB+',
'Caucasian',
'Female',
true,
'This person is absolutely crazy. Do not touch them.'
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (85, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good')
 ON DUPLICATE KEY UPDATE MID = MID;
 
 INSERT INTO declaredhcp(PatientID,HCPID) VALUE(85, 9000000003)
 ON DUPLICATE KEY UPDATE PatientID = PatientID;
 
 INSERT INTO representatives(RepresenterMID, RepresenteeMID) VALUES(85,2)
 ON DUPLICATE KEY UPDATE RepresenterMID = RepresenterMID;

INSERT INTO officeVisit
(patientMID, visitDate, locationID, apptTypeID, notes, weight, height, blood_pressure, household_smoking_status, patient_smoking_status, hdl, ldl, triglyceride) 
VALUES (85, '2016-07-28 11:00:00', 1, 7, 'Note', 102, 63, '104/76', 1, 4, 45, 81, 105);

SET @visit_id_2 = LAST_INSERT_ID();

INSERT INTO pregnancies
(id, birth_date, conception_year, days_pregnant, hours_labor, weight_gain, delivery_type, amount)
VALUES 
(85, '1971-01-01 00:00:00', 1970, 280, 9, 50, 'miscarriage', 1),
(85, '1972-02-01 00:00:00', 1972, 300, 9, 50, 'caesarean', 2),
(85, '1973-03-01 00:00:00', 1973, 290, 1, 41.1, 'forceps', 3),
(85, '1974-01-01 00:00:00', 1974, 290, 1, 40, 'vacuum', 4),
(85, '1991-01-04 00:00:00', 1990, 290, 1, 40, 'vaginal', 1);

INSERT INTO obstetrics_inits(id, init_date, lmp_date) Values(85, '2015-01-01', '2015-02-01');

INSERT INTO obstetrics_visits
(id, visitID, visitDate, weeksPregnant, weight, bloodPressure, fetalHR, amount, lowLying, rhFlag)
VALUES
(85, @visit_id_2, '2016-07-28 11:00:00', 30, 125.7, '120/60', 120, 1, false, true);

INSERT INTO ndcodes(Code, Description) VALUES
('50090-0015', 'Sulfamethoxazole and Trimethoprim'),
('54868-0971-1', 'Erythromycin Ethylsuccinate And Sulfisoxazole Acetyl'),
('50090-0007', 'Codeine'),
('62211-0838', 'Aspirin'),
('50090-1609', 'Celecoxib'),
('50090-0538', 'Diclofenac Sodium')
ON DUPLICATE KEY UPDATE Code = Code;

INSERT INTO icdcode (code, name, is_chronic) VALUES
('O210', 'Hyperemesis Gravidarum', 0),
('E02', 'Hypothyroidism', 0),
('O24','Diabetes mellitus in pregnancy',0 ),
('C01', 'Malignant neoplasm of base of tongue',0 ),
('C10', 'Malignant neoplasm of oropharynx',0 ),
('O03.9', 'High genetic potential for miscarriage', 1)
ON duplicate key update code=code;

INSERT INTO diagnosis
(visitId, icdCode)
VALUES
(@visit_id_2, 'O210'),
(@visit_id_2, 'E02'),
(@visit_id_2, 'J45'),
(@visit_id_2, 'U21'),
(@visit_id_2, 'A5132'),
(@visit_id_2, 'A46'),
(@visit_id_2, 'O03.9');

insert into allergies
(PatientID, Description, FirstFound, Code)
Values
(85, 'Codeine', '2007-06-05 20:33:33', '50090-0007'),
(85, 'Sulfamethoxazole and Trimethoprim', '2007-06-05 20:33:33', '50090-0015'),
(85, '30 TABLET, FILM COATED in 1 BOTTLE (0002-4463-30)', '2007-06-05 20:33:33', '0002-4463'),
(85, 'Diclofenac Sodium', '2007-06-05 20:33:33', '50090-0538');