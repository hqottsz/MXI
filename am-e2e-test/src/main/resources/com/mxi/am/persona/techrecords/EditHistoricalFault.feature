@Ignore
@RunAMTechnicalRecords
Feature: Edit a Historic Fault

	As a Technical Records Clerk
	I would like to edit a historical fault
	So that the resolution config slot is correct

	Background:
		Given I am a technical records clerk

	@SmokeTest @SetupHistoricalFault
	Scenario: Technical Records Clerk sets a resolution config slot on a historical fault
    	Given there is an existing historical fault that I would like to edit
    	When I change or enter a resolution config slot
    	Then the fault is updated with the changes
