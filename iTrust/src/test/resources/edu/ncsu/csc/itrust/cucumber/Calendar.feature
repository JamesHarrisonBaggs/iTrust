#Author: mjlilly

Feature: Calendar
As a Patient
I want to be able to look at upcoming appointments
So that I can plan my schedule

Scenario: Logged in as a Patient
Given Random Person has an appointment for tomorrow
And Random Person logs in with MID 1
When Random Person clicks View, then Full Calendar
Then Random Person should be taken to a page where they can view their upcoming appointments