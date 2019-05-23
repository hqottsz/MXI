
@RunAMLine
Feature: Add Part Requirement Search


    @E2ESmokeBuild1
    Scenario: User assigns a part to a task from the Add Part Requirement Search page
        Given I am a line technician
        When I add a part requirement to a task
        Then the part requirement is assigned to the task

