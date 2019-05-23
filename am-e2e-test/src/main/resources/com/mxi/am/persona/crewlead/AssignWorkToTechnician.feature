Feature: Assign Work to Technician

  Persona: Crew Lead
  Squad: Operator Base Maintenance

  Background:
    Given I am a superuser

  @E2ESmokeBuild1
  @ReviewWorkAssignedToCrewAndSkillsRequiredScenarioDataSetup
  Scenario: Review Work Assigned to Crew and Skills Required
    Given I have Work Packages
    And the Tasks in the Work Packages have Skills
    And I have a Crew
    And the Work Packages are Assigned to my Crew
    When I review the list of work assigned to my Crew
    Then the list of Tasks is visible
    And the Labor Skills are visible

  @E2ESmokeBuild1
  @IdentifyAvailResAndAssignWorkToTechScenarioDataSetup
  Scenario: Identify Available Resources and Assign Work to Technician
    Given I have Work Packages
    And the Tasks in the Work Packages have Skills
    And I have a Crew which has a Technician
    And the Work Packages are Assigned to my Crew
    When I view the details of an Assigned Task
    And I Assign the work to a Technician
    Then the work is assigned to the Technician
