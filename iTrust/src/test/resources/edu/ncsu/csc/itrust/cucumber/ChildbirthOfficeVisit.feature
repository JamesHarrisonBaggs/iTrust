Feature: Childbirth Office Visit
As an OB/GYN HCP
I want to able to document childbirth office visit for patient
so the childbirth will be saved and newborns will be added to the system

Scenario Outline: New Childbirth Office Visit
	Given an OB/GYN HCP has logged in with MID <hcp> and password <pw>
	And selects Document Office Visit in Obstetrics Info
	And enters patient's name <name> and chooses the correct MID <patient>
	And clicks on Create a New Office Visit
	When enters Date <date> and appointment type as Childbirth and click save
	And chooses delivery type <type>, and enter drugs <pi>, <no>, <pe>, <ea>, and <ms>, and Pre-Estimate Flag <flag>, then click Save Childbirth Visit
	And clicks New Baby, enters time <time>, and sex <sex>, then click Save
	Then clicks Save Childbirth Visit, a new Childbirth Office Visit is created and new patient is added
	
	Examples:
	| hcp       | pw | name | patient | date              | type             | pi | no | pe | ea | ms | flag  | time              | sex  |
	|9000000012 | pw | Andy | 2       | 4/3/2017 10:00 AM | Vaginal Delivery | 1  | 1  | 1  | 1  | 1  | false | 4/3/2017 10:00 PM | male |
	
Scenario Outline: Edit Childbirth Office Visit
	Given an OB/GYN HCP has logged in with MID <hcp> and password <pw>
	And selects Document Office Visit in Obstetrics Info
	And enters patient's name <name> and chooses the correct MID <patient>
	And clicks on an existed Childbirth Office Visit <date>
	When edit the delivery type <type>, and clicks Save Childbirth Visit
	Then the Childbirth Office Visit is modified
	
	Examples:
	| hcp       | pw | name | patient | date              | type                          |
	|9000000012 | pw | Andy | 2       | 4/3/2017 10:00 AM | Vaginal Delivery Vacuum Asist |
	
Scenario Outline: Invalid Patient
	Given an OB/GYN HCP has logged in with MID <hcp> and password <pw>
	And selects Document Office Visit in Obstetrics Info
	When types in the patient's name <name>
	Then the patient can not be found in system
	
	Examples:
	| hcp        | pw | patient |
	| 9000000012 | pw | 1111    |