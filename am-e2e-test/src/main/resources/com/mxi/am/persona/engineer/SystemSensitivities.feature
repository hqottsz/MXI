@Engineer @RunAMEngineering
Feature: Configure System Sensitivities

	Background:
	    Given I am an engineer

	@E2ESmokeBuild1
	@SystemSensitivitiesDataForView
	Scenario: View active sensitivities
		When I view a configuration slot's details with active system sensitivities
		Then the active system sensitivities are displayed

	@E2ESmokeBuild1
	@SystemSensitivitiesDataForCreate
	Scenario: Engineer creates a configuration slot with system sensitivities
		When I am creating a configuration slot
	    	And I enable system sensitivities on the configuration slot
		Then the configuration slot has the configured system sensitivities

	@E2ESmokeBuild1
	@SystemSensitivitiesDataForEdit
	Scenario: Engineer edits a configuration slot with system sensitivities
   	   Given I am editing a configuration slot
       Then I can see the current configured sensitivities for the configuration slot
       When I modify the sensitivities for the configuration slot
       Then the configuration slot is updated with the new sensitivity settings