Feature: Create sub task of fault

	As an engineer, I would like to initialize baseline requirements as corrective action subtasks of faults.
	Therefore, I should be able to create subtasks based on requirement task definition.

	Background:
		Given I am an engineer

@JspSmokeTest
	Scenario: create a sub-task based on a requirement task definition under a fault
    	When I attempt to create a sub task under an open fault
    	Then the effective from information is not visible while confirming the selection