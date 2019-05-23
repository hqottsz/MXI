@Engineer @RunAMEngineering
Feature: Part Group Sensitivities
	As a system engineer
	I need to be able to model part groups as being sensitive
	So that part group sensitivity warnings can be displayed

	@E2ESmokeBuild1
	@EditPartGroupSensitivitiesSetupData
	Scenario: Engineer edits a part group with sensitivities
		Given I am an engineer
          And I am editing a part group
        When I modify the sensitivities for the part group
        Then the part group is updated with the new configured sensitivities