#Author: ereinst

Feature: View Basic Health Information
As a HCP
I want to be able to view patient health information
so that I can know basic information about the patient's health
	
Scenario Outline: View Health Information
	Given I have logged in as HCP: <HCPuser> with password: <HCPpw> 
	And I select "iTrust - Basic Health Information"
	When I input values name <firstName>, MID <mid> and select the patient
	Then I can see the health information of patient <firstName> <lastName>

	Examples:
	| mid    | password | firstName | lastName	| HCPuser	| HCPpw			|
	| 1		 | pw       | Random    | Person	| 9000000000| pw			|	
	| 2		 | pw       | Andy      | Programmer| 9000000000| pw			|	
	| 5		 | pw       | Baby      | Programmer| 9000000000| pw			|	
	| 6		 | pw       | Baby      | Programmer| 9000000000| pw			|
	| 7		 | pw       | Baby	    | Programmer| 9000000000| pw			|	
	| 8		 | pw       | Baby	    | Programmer| 9000000000| pw			|