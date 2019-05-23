@RunAMLine
@LineTechnician
Feature: Batch Complete Tasks

  As a Line Technician
    I want to be able to batch complete tasks within a work package
    So that when I have already physically completed several tasks on the aircraft I don't have to manually perform several actions to complete them in Maintenix


  @E2ESmokeBuild1
  Scenario: Line Technician batch completes all tasks in a work package
    Given I am a line technician
    When I batch complete all tasks in a work package
    Then the tasks are completed
