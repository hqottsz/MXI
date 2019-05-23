@RunAMBaseMaintenance
Feature: Create Temporary Crew Assignment

  # Add Ignore, because it failed intermittent OPER-27844.
  # Identified as intermittent and enabled again under OPER-27844.
  @E2ESmokeBuild1
  Scenario: Crew Lead creates temporary crew assignment
    Given I am a crew lead
     When I temporarily assign a technician to a crew for multiple days
      And unassign from crew for one day
     Then shift adjustments and crew change schedules can be seen
