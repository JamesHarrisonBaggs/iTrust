INSERT INTO officeVisit
(patientMID, visitDate, locationID, apptTypeID, notes, weight, height, blood_pressure, household_smoking_status, patient_smoking_status, hdl, ldl, triglyceride) 
VALUES (2, '2017-02-28 11:00:00', 1, 7, 'Note', 102, 63, '104/76', 1, 4, 45, 81, 105);

SET @visit_id = LAST_INSERT_ID();

INSERT INTO obstetrics_visits
(id, visitID, visitDate, weeksPregnant, weight, bloodPressure, fetalHR, amount, lowLying, rhFlag)
VALUES
(2, @visit_id, '2017-02-28 11:00:00', 20, 125.7, '120/60', 120, 1, false, true);

INSERT INTO ultrasounds
(id, visitID, visitDate, fetus, crl, bpd, hc, fl, ofd, ac, hl, efw)
VALUES
(2, @visit_id, '2017-02-28 11:00:00', 1, 80, 40, 130, 25, 40, 110, 30, 201.55),
(2, @visit_id, '2017-02-28 11:00:00', 2, 80, 40, 130, 25, 40, 110, 30, 201.55);