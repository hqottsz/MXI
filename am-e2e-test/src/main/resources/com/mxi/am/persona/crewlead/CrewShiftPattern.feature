@RunAMBaseMaintenance
Feature: Crew shift pattern

	@E2ESmokeBuild1
	Scenario: Assign Shift Pattern to Crew
		Given I am a crew lead
		When I locate my crew
		And I assign shift pattern to crew
		Then shift pattern is assigned to crew