#Author: mcgibson
# add more scenarios

Feature: Edit Personnel Records

Scenario Outline: Valid Staff Info
	Given I log in as the HCP with MID <hcp> and password <pwd>
	When I enter a <validHCPFirstName>, <validHCPLastName>, <validHCPAddress>, <validCity>, <validState>, <validZip>, <validPhone>, and <validEmail> to the edit page
	Then the staff page should say Information Successfully Updated with <hcp>

Examples:
	|hcp    	| pwd	| validHCPFirstName    | validHCPLastName    | validHCPAddress     | validCity | validState | validZip | validPhone  	| validEmail   		|
	|9000000003	| pw	| Jack                 | Johnson             | 1910 Hover Drive    | Robot     | New York   | 20939    | 999-111-2222 	| crazy@mail.com 	|
	|9000000003	| pw	| Xena                 | Jackson             | 1111 Lillard Street | Robot     | New York   | 20993    | 999-111-2222 	| cra@mail.com   	|
