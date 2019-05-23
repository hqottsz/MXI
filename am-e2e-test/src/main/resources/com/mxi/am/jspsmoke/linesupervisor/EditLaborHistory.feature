
Feature: Edit labor history

 	As a line supervisor
	I am responsible for auditing the labor hours recorded in their work packages and correcting any discrepancies.
	I should be able to adjust the hours on labour row from the labour tab on a historic work package.

@E2ESmokeBuild1
  Scenario: User should be able to update the labor history of a work package
    Given I am a line supervisor
    And I attempt to schedule and start the open work package
    And I finish the labor of the task
    When I try to edit the labor history of the work package
    Then The labor history is successfully edited