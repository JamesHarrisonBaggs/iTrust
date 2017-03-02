#Author: amburns

Feature: View Inbox Message
	As an LHCP user
	I want to be able to view my inbox messages
	So I can keep track of what messages I have read

Scenario: View Message in Inbox  
	Given I log in as HCP 9000000000 
	And I navigate to the inbox
	And I send a new message and log out
	And I log in as a patient and respond to the message
	When I log back in as the HCP
	And click on the unread message
	And click the browser's back buttons
	Then I should be returned to the user's inbox
#	And the message should be designated as read and un-bolded	