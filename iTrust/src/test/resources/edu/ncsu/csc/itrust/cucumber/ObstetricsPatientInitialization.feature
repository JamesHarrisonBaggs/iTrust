Feature: Obstetrics Patient Initialization
As an OBGYN HCP
I want to be able to view, create, and edit patient's pregnancy record
so I can track patient's obstertric records 

Scenario Outline: Create Initialization
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Obstetric Care
	And I type in patient's MID <patient> and name <name> and select the patient
	And I click on create new initialization
	When I enter the patient's LMP <date>
	And I click Save Obstetrics Initialization
	And I can see Expected Delivery Date and Weeks Pregnant <date>
	And I click Return to Obstetrics Care
	Then a new obstetric initialization is created <date>
	
	Examples:
	| hcp   	 | pw | patient | name | date      |
	| 9000000012 | pw | 2       | Andy | 2/21/2017 |
	
#Scenario Outline: Edit Initialization
#	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
#	And I select Obstetrics Info, then go to Obstetric Care
#	And I enter patient's name <name> and select the patient
#	And I select the patient's initialization at <time>
#	When I choose to edit the patient's LMP <date>
#	And I click save
#	Then the initialization is changed
	
Scenario Outline: View Initialization
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Obstetric Care
	And I type in patient's MID <patient> and name <name> and select the patient
	When I select the patient's initialization at <time>
	Then I can view the initialization
	
	Examples:
	| hcp  		 | pw | patient | name | date     |
	| 9000000012 | pw | 2       | Andy | 1/1/2017 |	

Scenario Outline: New Prior Pregnancy
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Obstetric Care
	And I type in patient's MID <patient> and name <name> and select the patient
	And I select record prior pregnancy
	And I enter date of birth <date>, year of conception <yc>, Number of week pregnant <wk>, Number of hours in labor <hrs>, Weight gained during pregnancy <gain>, Delivery type <type>, Number of multiple <num>
	And I select create new prior pregnancy
	And I select return to obstetrics care
	Then a new pregnancy is created <time>
	
	Examples:
	| hcp        | pw | patient | name   | date      | yc   | wk | hrs | gain | type             | num | time      |
	| 9000000012 | pw | 1       | Random | 2/21/2017 | 2015 | 40 | 13  | 20   | Vaginal Delivery | 1   | 2/21/2017 |

#Scenario Outline: Invalid Pregnancy Input
#	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
#	And I select Obstetrics Info, then go to Obstetric Care
#	And I type in patient's MID <mid> and name <name> and select the patient
#	And I select record prior pregnancy
#	And I enter date of birth <dob>, year of conception <yc>, Number of week pregnant <wk>, Number of hours in labor <hrs>, Weight gained during pregnancy <gain>, Delivery type <type>, Number of multiple <num>
#	Then I select create new prior pregnancy but see an error
#	
#	Examples:
#	| hcp        | pw | mid | name | dob       | yc   | wk | hrs | gain | type             | num |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 | 2015 | -1 | 13  | 15   | Vaginal Delivery | 1   |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 | 2015 | 40 | -1  | 15   | Vaginal Delivery | 1   |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 | 2015 | 40 | 13  | -1   | Vaginal Delivery | 1   |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 | 2015 | 40 | 13  | 15   | asdf             | -1  |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 | 2015 | 40 | 13  | 15   | Vaginal Delivery | -1  |
	
#Scenario Outline: Empty Pregnancy Input
#	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
#	And I select Obstetrics Info, then go to Obstetric Care
#	And I type in patient's MID <mid> and name <name> and select the patient
#	And I select record prior pregnancy
#	And I enter date of birth <dob>, year of conception <yc>, Number of week pregnant <wk>, Number of hours in labor <hrs>, Weight gained during pregnancy <gain>, Delivery type <type>, Number of multiple <num>
#	Then I select create new prior pregnancy but see an error
#	
#	Examples:
#	| hcp        | pw | mid | name | dob       | yc   | wk | hrs | gain | type             | num |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 |      | 40 | 13  | 15   | Vaginal Delivery | 1   |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 | 2015 |    | 13  | 15   | Vaginal Delivery | 1   |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 | 2015 | 40 |     | 15   | Vaginal Delivery | 1   |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 | 2015 | 40 | 13  |      | Vaginal Delivery | 1   |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 | 2015 | 40 | 13  | 15   |                  | 1   |
#	| 9000000012 | pw | 2   | Andy | 2/21/2017 | 2015 | 40 | 13  | 15   | Vaginal Delivery |     |

Scenario Outline: Wrong Patient Name
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Obstetric Care
	And I enter patient's name <name> and the patient is not in the system
	When I re-enter the patient's MID <rpatient> and name <rname> and select the patient
	Then I can see the Patient's obstetric initialization page
	
	Examples:
	| hcp 		 | pw | name | rpatient | rname |
	| 9000000012 | pw | Sndy | 2        | Andy  |
	
Scenario Outline: Wrong Patient Choice
	Given I logged in as an OBGYN HCP with MID <hcp> and password <pw>
	And I select Obstetrics Info, then go to Obstetric Care
	And I type in patient's MID <patient> and name <name> and select the patient
	And I realize it is a wrong patient, so I click on Select a Different Patient
	When I re-enter the patient's MID <rpatient> and name <rname> and select the patient
	Then I can see the Patient's obstetric initialization page
	
	Examples:
	| hcp        | pw | patient | name   | rpatient | rname |
	| 9000000012 | pw | 1       | Random | 2        | Andy  |
	