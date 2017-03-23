Feature: Obstetrics Patient Initialization
As an OBGYN HCP
I want to be able to view, create, and edit patient's pregnancy record
so I can track patient's obstertric records 

Scenario Outline: Create Initialization
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Patient Obstetric Initialization
	And I enter patient's MID <patient> and name <name> and select the patient
	And I click on create new initialization
	When I enter the patient's LMP <date>
	And I click Calculate Estimated Delivery Date, And Save Initilization and Return to Obstetrics Home
	Then a new initialization <time> is created
	
	Examples:
	| hcp   	 | pw | patient | name | date       | time       |
	| 9000000012 | pw | 2       | Andy | 2017-02-21 | 2017-02-21 |	
	
#Scenario Outline: Edit Initialization
#	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
#	And I select Obstetrics Info, then go to Patient Obstetric Initialization
#	And I enter patient's name <name> and select the patient
#	And I select the patient's initialization at <time>
#	When I choose to edit the patient's LMP <date>
#	And I click save
#	Then the initialization is changed
	
Scenario Outline: New Prior Pregnancy
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Patient Obstetric Initialization
	And I enter patient's MID <patient> and name <name> and select the patient
	And I click on create new initialization
	When I enter the patient's LMP <date>
	And I add a new prior pregnancy, Year of conception <yc>, Number of week pregnant <wk>, Number of hours in labor <hrs>, Weight gained during pregnancy <gain>, Delivery type <type>, Number of multiple <num>
	And I click Calculate Estimated Delivery Date, And Save Initilization and Return to Obstetrics Home
	Then a new initialization <time> is created
	
	Examples:
	| hcp        | pw | patient | name   | date       | yc   | wk | hrs | gain | type             | num | time       |
	| 9000000012 | pw | 1       | Random | 2017-02-21 | 2015 | 40 | 13  | 20   | Vaginal Delivery | 1   | 2017-02-21 |
	
Scenario Outline: View Initializaition
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Patient Obstetric Initialization
	And I enter patient's MID <patient> and name <name> and select the patient
	When I select the patient's initialization at <time>
	Then I can view the initialization
	
	Examples:
	| hcp  		 | pw | patient | name | date       |
	| 9000000012 | pw | 2       | Andy | 2016-01-01 |	
	
Scenario Outline: Wrong Patient Name
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Patient Obstetric Initialization
	And I enter patient's MID <patient> and name <name> and select the patient
	And the patient is not in the system
	When I re-enter the patient's MID <rpatient> and name <rname> and select the patient
	Then I can see the Patient's obstetric initialization page
	
	Examples:
	| hcp 		 | pw | patient | name | rpatient | rname |
	| 9000000012 | pw | 2       | Sndy | 2        | Andy  |
	
Scenario Outline: Wrong Patient Choice
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Patient Obstetric Initialization
	And I enter patient's MID <patient> and name <name> and select the patient
	And I realize it is a wrong patient, so I click on Select a Different Patient
	When I re-enter the patient's MID <patient> and name <rname> and select the patient
	Then I can see the Patient's obstetric initialization page
	
	Examples:
	| hcp        | pw | patient | name | rpatient | rname |
	| 9000000012 | pw | 12      | Andy | 2        | Andy  |
	
Scenario Outline: Incorrect Prior Pregnancy Input
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Patient Obstetric Initialization
	And I enter patient's MID <patient> and name <name> and select the patient
	And I click on create new initialization
	When I enter the patient's LMP <date>
	And I add a new prior pregnancy, Year of conception <yc>, Number of week pregnant <wk>, Number of hours in labor <hrs>, Weight gained during pregnancy <gain>, Delivery type <type>, Number of multiple <num>
	And I click Calculate Estimated Delivery Date, And Save Initilization and Return to Obstetrics Home
	Then the system asked to enter correct prior pregnancy info
	
	Examples:
	| hcp        | pw | patient | name | date       | yc   | wk | hrs | gain | type             | num |
	| 9000000012 | pw | 2       | Andy | 2017-02-21 |      | 40 | 13  | 15   | Vaginal Delivery | 1   |
	| 9000000012 | pw | 2       | Andy | 2017-02-21 | 2015 |    | 13  | 15   | Vaginal Delivery | 1   |
	| 9000000012 | pw | 2       | Andy | 2017-02-21 | 2015 | 40 |     | 15   | Vaginal Delivery | 1   |
	| 9000000012 | pw | 2       | Andy | 2017-02-21 | 2015 | 40 | 13  |      | Vaginal Delivery | 1   |
	| 9000000012 | pw | 2       | Andy | 2017-02-21 | 2015 | 40 | 13  | 15   |                  | 1   |
	| 9000000012 | pw | 2       | Andy | 2017-02-21 | 2015 | 40 | 13  | 15   | Vaginal Delivery |     |
	