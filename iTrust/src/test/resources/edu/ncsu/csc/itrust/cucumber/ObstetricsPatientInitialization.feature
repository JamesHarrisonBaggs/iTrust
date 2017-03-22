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
	And I click create
	Then a new initialization is created
	
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
	And I add a new prior pregnancy, Year of conception <yc>, Number of week pregnant <wk> <day>, Number of hours in labor <hrs>, Weight gained during pregnancy <gain>, Delivery type <type>, Whether is a multiple <multiple>, How many <num>
	And I click create
	Then a new initialization is created
	
Scenario Outline: View Initializaition
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Patient Obstetric Initialization
	And I enter patient's MID <rpatient> and name <name> and select the patient
	When I select the patient's initialization at <time>
	Then I can view the initialization
	
Scenario Outline: Wrong Patient Name
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Patient Obstetric Initialization
	And I enter patient's MID <patient> and name <name> and select the patient
	And the patient is not in the system
	When I re-enter the patient's MID <rpatient> and name <rname> and select the patient
	Then I can see the Patient's obstetric initialization page
	
Scenario Outline: Wrong Patient Choice
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Patient Obstetric Initialization
	And I enter patient's MID <patient> and name <name> and select the patient
	And I realize it is a wrong patient, so I click on Select a Different Patient
	When I re-enter the patient's MID <patient> and name <rname> and select the patient
	Then I can see the Patient's obstetric initialization page
	
Scenario Outline: Incorrect Prior Pregnancy Input
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Patient Obstetric Initialization
	And I enter patient's MID <patient> and name <name> and select the patient
	And I click on create new initialization
	When I enter the patient's LMP <date>
	And I add a new prior pregnancy, Year of conception <yc>, Number of week pregnant <wk> <day>, Number of hours in labor <hrs>, Weight gained during pregnancy <gain>, Delivery type <type>, Whether is a multiple <multiple>, How many <num>
	And I click create
	Then the system asked to enter correct prior pregnancy info
	