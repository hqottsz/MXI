@RunAMBaseMaintenance
Feature: Perform Workscope Planning

  Persona: Workscope Planner
  Squad: Operator Base Maintenance

  Background:
    Given I am a superuser

  @E2ESmokeBuild1
  Scenario: Review Planned Visit
    Given I have an Aircraft "Aircraft Part MOC 1 - 20984-01"
    And the Aircraft has an Open Work Package "WP20984-01"
    When I review the Workscope
    Then the Workscope exists

  @E2ESmokeBuild1
  Scenario: Determine Where Visit will be performed
    Given I have an Aircraft "Aircraft Part MOC 1 - 20984-02"
    And the Aircraft has an Open Work Package "WP20984-02"
    When I Schedule the Work Package
    Then the Work Package is Scheduled

  # Add Ignore, because it failed intermittent OPER-27845.
  # Identified as intermittent and enabled again under OPER-27845.
  @E2ESmokeBuild1
  Scenario: Review Upcoming Open Items on Aircraft (Unassigned Faults)
    Given I have an Aircraft "Aircraft Part MOC 1 - 20984-03"
    And the Aircraft has an Open Work Package "WP20984-03"
    And the Aircraft has Unassigned Faults
    When I review the Unassigned Faults
    Then the Faults "F20984-1" exist

  @E2ESmokeBuild1
  Scenario: Review Upcoming Open Items on Aircraft (Unassigned Tasks)
    Given I have an Aircraft "Aircraft Part 2 - 20984-04"
    And the Aircraft has an Open Work Package "WP20984-04"
    And the Aircraft has Unassigned Tasks
    When I review the Unassigned Tasks
    Then the Tasks "SYS-REQ-LEADER (System Requirement - Leader Task)" exist

  @E2ESmokeBuild1 @Ignore
  Scenario: Add Items to Workscope
    Given I have an Aircraft "Aircraft Part 2 - 20984-05"
    And the Aircraft has an Open Work Package "WP20984-05"
    And the Aircraft has Unassigned Faults
    Then I review the Unassigned Faults
    And I Assign Unassigned Faults "F20984-5" to the Work Package
    And the Aircraft has Unassigned Tasks
    Then I review the Unassigned Tasks
    And I Assign Unassigned Tasks "SYS-REQ-LEADER (System Requirement - Leader Task)" to the Work Package
    Then the Unassigned Faults "F20984-5" are Assigned to the Work Package
    And the Unassigned Tasks "SYS-REQ-LEADER (System Requirement - Leader Task)" are Assigned to the Work Package

  @E2ESmokeBuild1
  Scenario: Determine is Workscope should be Locked (Commit Work Package)
    Given I have an Aircraft "Aircraft Part MOC 1 - 20984-06"
    And the Aircraft has an Open Work Package "WP20984-06"
    And the Work Package has Tasks "SCH_WORK_TSK1 (SCH_WORK_TSK1)" Assigned
    And the Work Package has Faults "F20984-7" Assigned
    When I Commit the Workscope
    Then the Work Package is Committed