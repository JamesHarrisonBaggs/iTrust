#Author: ereinst

Feature: Health Tracker Data
As a HCP
I want to be able to view, edit, and upload patient health tracker data
so that I can track patient health data over time

Scenario Outline: Enter and View Health Tracker Data
	Given I have logged in as the HCP with MID <hcp> and password <pwd>
	And I select Patient Information, then Health Tracker Data
	And I input patient first name <name> and MID <mid> and select the patient
	And select Edit to navigate to the Edit page
	When I enter date <date>, calories <cals>, steps <steps>, distance <dist>, floors <fs>, activity calories <acal>, minutes sedentary <mins>, lightly active <minl>, fairly active <minf>, very active <minv>, active hours <ahrs>, heartrate low <hlo>, heartrate high <hhi>, heartrate average <havg>, and UV exposure <uvex>
	And click the button to save the data
	And select View to navigate to the View page
	And enter the date to view <date>
	Then I can view the data I entered
	
	Examples:
	| hcp 			| pwd	| name	| mid	| date		| cals 	| steps | dist | fs | acal	| mins 	| minl	| minf 	| minv 	| ahrs	| hlo	| hhi	| havg	| uvex	|
	| 9000000000	| pw	| Andy	| 2		| 10/1/2016	| 2455	| 11017 | 4.89 | 15	| 1448	| 970	| 463	| 2		| 5		| 5		| 64	| 83	| 71	| 3		|
	| 9000000000 	| pw	| Andy	| 2		| 10/2/2016	| 2603	| 13537 | 6.01 | 15 | 1596 	| 967	| 406	| 39	| 28	| 3		| 47	| 116	| 71	| 1		|
	| 9000000000 	| pw	| Andy	| 2		| 10/3/2016	| 2126	| 6833  | 3.03 | 16 | 915	| 1161	| 270	| 5		| 4		| 4		| 58	| 103	| 70	| 0		|

Scenario Outline: Enter Negative Health Tracker Data
	Given I have logged in as the HCP with MID <hcp> and password <pwd>
	And I select Patient Information, then Health Tracker Data
	And I input patient first name <name> and MID <mid> and select the patient
	And select Edit to navigate to the Edit page
	When I enter date <date>, calories <cals>, steps <steps>, distance <dist>, floors <fs>, activity calories <acal>, minutes sedentary <mins>, lightly active <minl>, fairly active <minf>, very active <minv>, active hours <ahrs>, heartrate low <hlo>, heartrate high <hhi>, heartrate average <havg>, and UV exposure <uvex>
	And click the button to save the data
	Then an error message is displayed at the top: <error>
	
	Examples:
	| hcp 			| pwd	| name	| mid	| date		| cals 	| steps | dist | fs | acal	| mins 	| minl	| minf 	| minv 	| ahrs	| hlo	| hhi	| havg	| uvex	| error 																													|
	| 9000000000	| pw	| Andy	| 2		| 10/1/2016	| -1	| 11017 | 4.89 | 15	| 1448	| 970	| 463	| 2		| 5		| 5		| 64	| 83	| 71	| 3		| This form has not been validated correctly. The following field are not properly filled in: [Calories cannot be negative] |

Scenario Outline: Enter Non-Numerical Health Tracker Data
	Given I have logged in as the HCP with MID <hcp> and password <pwd>
	And I select Patient Information, then Health Tracker Data
	And I input patient first name <name> and MID <mid> and select the patient
	And select Edit to navigate to the Edit page
	When I enter date <date>, calories <cals>, steps <steps>, distance <dist>, floors <fs>, activity calories <acal>, minutes sedentary <mins>, lightly active <minl>, fairly active <minf>, very active <minv>, active hours <ahrs>, heartrate low <hlo>, heartrate high <hhi>, heartrate average <havg>, and UV exposure <uvex>
	And click the button to save the data
	Then an error message is displayed: <error>
	
	Examples:
	| hcp 			| pwd	| name	| mid	| date		| cals 	| steps | dist | fs | acal	| mins 	| minl	| minf 	| minv 	| ahrs	| hlo	| hhi	| havg	| uvex	| error 																													|
	| 9000000000	| pw	| Andy	| 2		| 10/1/2016	| 2455	| 2.13  | 4.89 | 15	| 1448	| 970	| 463	| 2		| 5		| 5		| 64	| 83	| 71	| 3		| htform:steps: '2.13' must be a number between -2147483648 and 2147483647													|
	| 9000000000	| pw	| Andy	| 2		| 10/1/2016	| asd	| 11017 | 4.89 | 15	| 1448	| 970	| 463	| 2		| 5		| 5		| 64	| 83	| 71	| 3		| htform:calories: 'asd' must be a number between -2147483648 and 2147483647 Example: 9346									|
	| 9000000000	| pw	| Andy	| 2		| 10/1/2016	| 2455	| 11017 | as0d | 15	| 1448	| 970	| 463	| 2		| 5		| 5		| 64	| 83	| 71	| 3		| htform:distance: 'as0d' must be a number between 4.9E-324 and 1.7976931348623157E308 										|
	# Entering a double in an int field
	# Entering a non-number in an int field
	# Entering a non-number in a double field
	
	# STILL NEED TO TEST: Entering nothing into a field
	# (htform:calories: Validation Error: Value is required.)

Scenario Outline: Upload and View Health Tracker Data
	Given I have logged in as the HCP with MID <hcp> and password <pwd>
	And I select Patient Information, then Health Tracker Data
	And I input patient first name <name> and MID <mid> and select the patient
	And select Upload to navigate to the Upload page
	When I click the button to upload the file <filename>
	And select View to navigate to the View page
	And enter the date to view <date>
	Then I can view the data: <cals>, <steps>, <dist>, <fs>, <mins>, <minl>, <minf>, <minv>, and <acal>
	
	Examples:
	| hcp 			| pwd	| name	| mid	| filename														| date			| cals 	| steps | dist | fs | mins 	| minl	| minf 	| minv 	| acal 	|
	| 9000000000	| pw	| Andy	| 2		| /testing-files/sample_healthtracker/fitbit-test-data.csv	| 10/1/2016		| 2455	| 11017 | 4.89 | 15	| 970	| 463	| 2		| 5		| 1448	|
	| 9000000000 	| pw	| Andy	| 2		| /testing-files/sample_healthtracker/fitbit-test-data.csv	| 10/2/2016		| 2603	| 13537 | 6.01 | 15 | 967	| 406	| 39	| 28	| 1596	|
	| 9000000000 	| pw	| Andy	| 2		| /testing-files/sample_healthtracker/fitbit-test-data.csv	| 10/31/2016	| 2212	| 7137  | 3.17 | 16 | 1143	| 285	| 5		| 7		| 1015	|

Scenario Outline: View Health Tracker Summary Data
	Given I have logged in as the HCP with MID <hcp> and password <pwd>
	And I select Patient Information, then Health Tracker Data
	And I input patient first name <name> and MID <mid> and select the patient
	And select Upload to navigate to the Upload page
	When I click the button to upload the file <filename>
	And select Summary to navigate to the Summary page
	Then I can see a summary of the data
	
	Examples:
	| hcp 			| pwd	| name	| mid	| filename													|
	| 9000000000	| pw	| Andy	| 2		| /testing-files/sample_healthtracker/fitbit-test-data.csv	|