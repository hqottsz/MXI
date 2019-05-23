Feature: Assign Work to Crew

  Persona: Production Controller
  Squad: Operator Base Maintenance

  Background:
    Given I am a superuser

  @E2ESmokeBuild1
  @ReviewInProgressWorkScenarioDataSetup
  Scenario: Review In Progress Work
    Given I have a Work Package
    And the Work Package has Assigned Tasks
    And the Workscope is Committed
    When I review the WorkScope
    Then the WorkScope exists

  @E2ESmokeBuild1
  @ReviewUnassignedWorkScenarioDataSetup
  Scenario: Review Unassigned Work
    Given I have a Work Package
    And the Work Package has Assigned Tasks
    And the Workscope is Committed
    When I review the Work Package Labor Tab
    Then there are Tasks that are not Assigned to Technicians
    And the Tasks have Labor Labor Skills

  @E2ESmokeBuild1
  @IdentifyAvailableResourcesScenarioDataSetup
  Scenario: Identify Available Resources
    Given I have a Work Package
    And the Work Package is Scheduled to a Location
    And the Location has a Crew
    And the Crew has a User
    And the User has a Schedule
    When I view the User Schedule
    Then the Schedule exists

  @E2ESmokeBuild1
  @AssignWorkPackageToCrewScenarioDataSetup
  Scenario: Assign Work Package to Crew
    Given I have a Work Package
    And the Work Package has Task
    And the Work Package is Scheduled to a Location
    And the Location has a Crew
    And the Crew has a User
    When I Assign the Work Package to a Crew
    Then the Work Package is Assigned to the Crew
    And I can see the Tasks on the Users Assigned to My Crew tab

  @E2ESmokeBuild1
  @AssignWorkToCrewScenarioDataSetup
  Scenario: Assign Work to Crew
    Given I have a Work Package
    And the Work Package has Tasks
    And the Work Package is Scheduled to a Location
    And the Location has a Crew
    And the Crew has a User
    When I Plan Shift for the Work Package
    Then I can assign a Tasks to a Crew
    And I can unassign a Task from a Crew
    And I can see the Tasks on the Users Assigned to My Crew tab

  @Ignore
  @E2ESmokeBuild1
  @AssignWorkToCrewShiftDayScenarioDataSetup
  Scenario: Assign Work to Crew Shift Day
    Given I have a Work Package
    And the Work Package has Tasks
    And the Work Package is Scheduled to a Location
    And the Location has a Crew
    And the Crew has a Schedule
    And the Crew has a user
    And the work package is Committed
    When I Plan Shift for this Work Package
    Then I can assign a Tasks to a Crew Shift Day
    And I can unassign a Task from a Crew Shift Day
    And I can see the Tasks on the Users Assigned to My Crew tab
