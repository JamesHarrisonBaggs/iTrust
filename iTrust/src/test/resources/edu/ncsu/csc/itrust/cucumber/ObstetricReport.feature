Feature: Obstetric Report
As an OBGYN HCP
I want to be able view a full report of patient's obstetric stats
so I can follow up with patient's future plan

Scenario: View Report
	Given I logged in with MID 9000000012 and password pw as an OBGYN HCP
	And I see patient Aaron with MID 85 has an obstetric initialization
	And I also see an OBGYN office visit
	When I go to Labor and Delivery Report
	Then I can see all above information is in the report
	
Scenario: Patient Does Not Exist
	Given I logged in with MID 9000000012 and password pw as an OBGYN HCP
	And I select Obstetrics Info, then go to Labor and Delivery Report
	When I type in patient's MID 1111
	Then the patient is not recorded in the system
	
Scenario: Patient is not eligible
	Given I logged in with MID 9000000012 and password pw as an OBGYN HCP
	And I select Obstetrics Info, then go to Labor and Delivery Report
	When I type in patient's MID 12
	Then the patient is not eligible for obstetric care