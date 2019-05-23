@RunAMLine
@LineSupervisor
Feature: Review Work Capture

	As a Line Supervisor
	I would like to review the work performed by a line technician
	So that I am able to sign off on the work completed

	# Ignore temporarily due to WebLogic 12.2.1
    @E2ESmokeBuild1 @ReviewWorkCaptureResolutionConfigSlot @Ignore
	Scenario: Line Supervisor reviews work capture after Line Technician sets a resolution config slot
        Given I am a line supervisor
        When I review work captured by a line technician
        Then the resolution config slot information is visible


