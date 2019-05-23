@RunAMBaseMaintenance
Feature: Receive Aircraft

  Persona: Aircraft Release Supervisor
  Squad: Operator Base Maintenance

  Background:
    Given I am a superuser

  @E2ESmokeBuild1
  Scenario: Review Aircraft Status (Current Usage)
    Given I have an Aircraft
    And the Aircraft has Usage
    When I review the Current Usage
    Then I can see the Current Usage

  @E2ESmokeBuild1
  Scenario: Review Aircraft Status (Open Faults)
    Given I have an Aircraft
    And the Aircraft has Open Faults
    When I review the list of Open Faults
    Then I can see the Open Faults

  @E2ESmokeBuild1
  Scenario: Update Work to be Done (Raise and Assign Faults)
    Given I have an Aircraft
    And the Aircraft has an Open Work Package
    When I Raise a Logbook Fault
    And I Assign the Fault to a Work Package
    Then the Fault exists
    And the Fault is Assigned to the Work Package

  @E2ESmokeBuild1
  Scenario: Identify Aircraft is in Maintenance (Start Work Package)
    Given I have an Aircraft
    And the Aircraft has a Committed Work Package
    When I Start the Work Package
    Then the Work Package is Started
