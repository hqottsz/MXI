@LinePlanner @RunAMLine
Feature: Display Sensitivity Alerts

	As a Line Planner
	I would like to be able to see Sensitivity Alerts
	So that I can plan accordingly to ensure the impacts are known,
	the proper skilled technicians/inspectors are available,
	or plan any follow on tasks or activities

	@E2ESmokeBuild1 @SensitivityWorkscopeData
    Scenario: Line Planner views Sensitivity Chips for faults which has enabled system sensitivities
    	Given I am a line planner
        When I view the workscope of a work package which contains a fault on a failed system with sensitivities
    	Then I see sensitivity chips indicating the sensitivities for the fault's failed system on the workscope