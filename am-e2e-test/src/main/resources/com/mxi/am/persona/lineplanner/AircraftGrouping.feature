@RunAMPlanning
@MaintenancePlanner
@MaintenanceController
Feature: Aircraft Grouping

   As a Maintenance Planner or Maintenance Controller,
   I want to define groups of aircraft
   So that I can use to filter fleet views to only include aircraft in my group


  Background:
    Given I am a line planner

    # First 3 lines should be kept as SmokeTest
    @E2ESmokeBuild1 @RefactorNonSmoke
	Scenario: Create Aircraft Group
		When I create a new aircraft group
		And I add aircraft to the group
		Then the group is created and includes the added aircraft
		And the group assignment list shows the aircraft in the new group

    # Ideally this scenario should be refactored, but it sets up data for scenarios 3 and 4
    # So we'll keep it for now, until we can refactor it later
	@E2ESmokeBuild1 @RefactorNonSmoke
	Scenario: Edit Aircraft Group
		When I edit an aircraft group
		And I update the aircraft group assignments
		Then the group is updated and includes the updated assignments
		And the group assignment list shows the updated group assignment information


	@E2ESmokeBuild1
	Scenario: Filtering Fleet List and Fleet Due List By Aircraft Group
		When I select an aircraft group
		Then the fleet view is filtered by aircraft group

	@E2ESmokeBuild1
	Scenario: Remove Aircraft Group
		When I delete an aircraft group
		Then the group is deleted
		And the group assignment list no longer shows the aircraft in the group