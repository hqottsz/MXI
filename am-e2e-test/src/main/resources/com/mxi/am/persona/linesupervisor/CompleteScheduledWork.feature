@RunAMLine
Feature: Complete Scheduled Work

	Background:
		Given I am a line supervisor

	@RefactorNonSmoke
	Scenario: Line Technician completes scheduled work and releases an aircraft back into service
        Given I have scheduled work for a line technician
        When the line technician completes the scheduled work
        Then the task status is marked as complete
        When I complete the work package
        Then the work package status is marked as complete
        	And the aircraft is released back into service

	@RefactorNonSmoke
	Scenario: Line Supervisor is unable to release an aircraft with a single overdue task
		Given an aircraft has a work package and an overdue open task
		When I attempt to complete the work package
		Then I should receive an error message about the overdue task
			And I see the overdue task listed in the Tasks table