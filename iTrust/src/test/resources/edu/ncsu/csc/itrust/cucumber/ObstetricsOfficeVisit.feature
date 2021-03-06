Feature: Obstetrics Office Visit
As an OBGYN HCP
I want to be able to create, edit obstetrics office visit for patient
so I can exam the condition of patient and her babies

#Scenario Outline: Create Office Visit
#	Given I logged in as an OB/GYN HCP with MID <hcp> and password <pw> for Obstetrics Office Visit
#	And I select Obstetrics Info, then go to Document Office Visit
#	And I enter patient's name <name> and select the patient MID <patient>
#	And I click Create a New Office Visit
#	When I enter Date <date> and choose appointment type as OB/GYN and click save
#	And I click on Obstetrics, and enter weight <weight>, blood pressure <bp>, FHR <fhr>, multiples <num>, LLP <llp>, RHflag <rh>, and click Save Obstetrics Visit
#	And I click on Add Ultrasound, and enter id <id>, CRL <crl>, BPD <bpd>, HC <hc>, FL <fl>, OFD <ofd>, AC <ac>, HL <hl>, EFW <efw>, and click save Ultrasound
#	And I click Go Back To Office Visit, and click Save Obstetrics Visit
#	Then a new Obstetrics Office Visit is created for the patient
#	And the next office visit will be scheduled at a federal holiday, so it changes to <appt>
#
#	Examples:
#	| hcp        | pw | name | patient | date                | weight | bp     | fhr | num | llp  | rh   | id | crl | bpd | hc | fl | ofd | ac | hl | efw |
#	| 9000000012 | pw | Andy | 2       | 01/20/2016 10:30 AM | 140    | 80/110 | 100 | 1   | true | true | 1  | 1   | 1   |	1  | 1  | 1   | 1  | 1  | 1   |
	
#Scenario Outline: Edit Office Visit
#	Given I logged in as an OB/GYN HCP with MID <hcp> and password <pw> for Obstetrics Office Visit
#	And I select Obstetrics Info, then go to Document Office Visit
#	And I enter patient's name <name> and select the patient MID <patient>
#	And I click on an exisited OBGYN office visit <date>
#	When I edit weight <weight>, and click save Obstetrics Visit
#	Then the Obstetrics Office Visit is changed
#	
#	Examples:
#	| hcp        | pw | name | patient | date               | weight |
#	| 9000000012 | pw | Andy | 2       | 7/28/2016 11:00 AM | 135    |
	
Scenario Outline: Patient Does Not Exist
	Given I logged in as an OB/GYN HCP with MID <hcp> and password <pw> for Obstetrics Office Visit
	And I select Obstetrics Info, then go to Document Office Visit
	When I enter patient's MID <patient>
	Then the patient is not exist
	
	Examples:
	| hcp        | pw | patient |
	| 9000000012 | pw | 1111    |
		
Scenario Outline: Patient Not Eligible
	Given I logged in as an OB/GYN HCP with MID <hcp> and password <pw> for Obstetrics Office Visit
	And I select Obstetrics Info, then go to Document Office Visit
	And I enter patient's name <name> and select the patient MID <patient>
	And I click Create a New Office Visit
	When I enter Date <date> and choose appointment type as OB/GYN and click save
	Then the patient is not eligible for obstetrics care
	
	Examples:
	| hcp        | pw | name   | patient | date                |
	| 9000000012 | pw | Zappic | 10      | 01/20/2016 10:30 AM |
	
#Scenario Outline: HCP Not An OB/GYN
#	Given I logged in as a none OB/GYN HCP with MID <hcp> and password <pw> for Obstetrics Office Visit
#	And I select Obstetrics Info, then go to Document Office Visit
#	And I enter patient's name <name> and select the patient MID <patient>
#	And I click Create a New Office Visit
#	When I enter Date <date> and choose appointment type as OB/GYN and click save
#	Then a message says I can't create an OBGYN appointment because I am not an OBGYN HCP
#	
#	Examples:
#	| hcp        | pw | name | patient | date                |
#	| 9000000000 | pw | Andy | 2       | 01/20/2016 10:30 AM |
	
	