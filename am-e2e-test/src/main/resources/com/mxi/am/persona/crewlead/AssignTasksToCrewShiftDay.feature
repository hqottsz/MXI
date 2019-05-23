@RunAMBaseMaintenance
Feature: Assign Tasks To Crew Shift Day

	# Ignore temporarily due to WebLogic 12.2.1
	@E2ESmokeBuild1 @Ignore
	Scenario: Assign Tasks to Crew Shift Day
		Given I am a crew lead
		When I assign tasks to crew shift day
		Then Tasks are assigned to crew shift day