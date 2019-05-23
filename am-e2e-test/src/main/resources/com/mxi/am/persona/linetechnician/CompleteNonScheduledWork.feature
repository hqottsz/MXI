@RunAMLine
@LineTechnician
Feature: Complete Non-Scheduled Work

  Background:
    Given I am a line technician
      And I want to complete non-scheduled work

	# Ignore temporarily due to WebLogic 12.2.1
	@E2ESmokeBuild1 @Ignore
	Scenario: Line Technician completes non-scheduled work
		When I complete the last open task in the work package
		Then the work package is ready to be closed
