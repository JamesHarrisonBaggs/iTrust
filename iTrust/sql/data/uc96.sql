INSERT INTO officeVisit
(patientMID, visitDate, locationID, apptTypeID, notes, weight, height, blood_pressure, household_smoking_status, patient_smoking_status, hdl, ldl, triglyceride) 
VALUES (2, '2017-04-05 11:30:00', 1, 7, 'Note', 102, 63, '104/76', 1, 4, 45, 81, 105);

SET @visit_id_1 = LAST_INSERT_ID();

INSERT INTO officeVisit
(patientMID, visitDate, locationID, apptTypeID, notes, weight, height, blood_pressure, household_smoking_status, patient_smoking_status, hdl, ldl, triglyceride) 
VALUES (7, '2017-04-03 9:30:00', 1, 7, 'No notes', 115.7, 71, '110/60', 1, 4, 46, 80, 103);

SET @visit_id_2 = LAST_INSERT_ID();

INSERT INTO childbirth_visits
(patientID, visitID, visitDate, preScheduled, deliveryType, pitocin, nitrousOxide, pethidine, epiduralAnaesthesia, magnesiumSO4)
VALUES
(2, @visit_id_1, '2017-04-05 11:30:00', true, 'vaginal', 1, 0, 3, 0, 4),
(7, @visit_id_2, '2017-04-03 9:30:00', false, 'caesarean', 0, 2, 0, 10, 6);

INSERT INTO childbirths
(parentID, visitID, birthDate, gender, estimated, added)
VALUES
(2, @visit_id_1, '2017-04-05 11:35:00', 'Female', false, false),
(7, @visit_id_2, '2017-04-03 8:30:00', 'Male', true, false);