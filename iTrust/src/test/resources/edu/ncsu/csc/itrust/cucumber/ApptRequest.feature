#Author: mjlilly & amburns

Feature: Request Appointment with HCP
As a Patient
I want to be able to request an appointment with my HCP
So that I can keep track of my health

Scenario: Logged in as a Patient with Designated HCP3
Given Andy Programmer logs in with MID 2
When Andy Programmer clicks Appointments, then Appointment Requests
And Makes an appointment request for tomorrow at 9:45AM
And logs out
And Gandalf Stormcrow logs in with MID 9000000003
And Gandalf Stormcrow clicks Appointments, then Appointment Requests
Then Gandalf Stormcrow should have one appointment request with Andy Programmer
And Kelly Doctor does not have an appointment request with Andy Programmer